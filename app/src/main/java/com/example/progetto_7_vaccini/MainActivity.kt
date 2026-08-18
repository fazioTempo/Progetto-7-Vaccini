package com.example.progetto_7_vaccini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.progetto_7_vaccini.data.BiologicType
import com.example.progetto_7_vaccini.data.MedicalCondition
import com.example.progetto_7_vaccini.data.Sex
import com.example.progetto_7_vaccini.data.VaccineRec
import com.example.progetto_7_vaccini.data.*
import com.example.progetto_7_vaccini.screens.*
import com.example.progetto_7_vaccini.ui.theme.VaccineBiologicTheme
import androidx.lifecycle.lifecycleScope
import androidx.compose.runtime.rememberCoroutineScope
import com.example.progetto_7_vaccini.data.database.DatabaseInitializer
import com.example.progetto_7_vaccini.data.database.DatabaseProvider
import kotlinx.coroutines.launch
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.Icons
import com.example.progetto_7_vaccini.data.database.entities.Sesso

fun Sex.toSesso(): Sesso = when (this) {
    Sex.MALE -> Sesso.MASCHIO
    Sex.FEMALE -> Sesso.FEMMINA
}

fun Sesso.toSex(): Sex = when (this) {
    Sesso.MASCHIO -> Sex.MALE
    Sesso.FEMMINA -> Sex.FEMALE
}

enum class AppScreen {
    LANDING, LOGIN, REGISTER, FORM, RESULTS, NEW_PASSWORD, LOGGED_USER, MODIFY_DATA
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val database = DatabaseProvider.getDatabase(applicationContext)
        lifecycleScope.launch {
            DatabaseInitializer.inizializza(database)
        }
        setContent {
            VaccineBiologicTheme {
                // Navigation state
                var currentScreen by rememberSaveable { mutableStateOf(AppScreen.LANDING) }
                var previousScreen by rememberSaveable { mutableStateOf(AppScreen.LANDING) }
                var userRole by rememberSaveable { mutableStateOf<String?>(null) } // "PAZIENTE", "MEDICO" o null (Ospite)
                var currentUserEmail by rememberSaveable { mutableStateOf<String?>(null) }
                var actualPassword by rememberSaveable { mutableStateOf("") }

                val coroutineScope = rememberCoroutineScope()
                
                // Form data state
                var results by rememberSaveable { mutableStateOf<List<VaccineRec>?>(null) }
                var patientName by rememberSaveable { mutableStateOf("") }
                var patientSurname by rememberSaveable { mutableStateOf("") }
                var patientBirthDate by rememberSaveable { mutableStateOf("") }
                var patientAge by rememberSaveable { mutableStateOf<Int?>(null) }
                var patientSex by rememberSaveable { mutableStateOf<Sex?>(null) }
                var patientBiologic by rememberSaveable { mutableStateOf<BiologicType?>(null) }
                var patientConditions by rememberSaveable { mutableStateOf(setOf<MedicalCondition>()) }
                var patientHistory by rememberSaveable { mutableStateOf(setOf<String>()) }
                var isEditingPatient by rememberSaveable { mutableStateOf(false) }
                var doctorPatients by rememberSaveable { mutableStateOf<List<com.example.progetto_7_vaccini.data.database.entities.Paziente>>(emptyList()) }

                @OptIn(ExperimentalMaterial3Api::class)
                when (currentScreen) {
                    AppScreen.LANDING -> {
                        LandingScreen(
                            onGuestClick = { 
                                // Reset dei dati paziente per l'ospite
                                patientName = ""
                                patientSurname = ""
                                patientBirthDate = ""
                                patientAge = null
                                patientSex = null
                                patientBiologic = null
                                patientConditions = emptySet()
                                patientHistory = emptySet()
                                results = null
                                
                                userRole = null
                                isEditingPatient = false
                                currentScreen = AppScreen.FORM
                            },
                            onLoginClick = { currentScreen = AppScreen.LOGIN },
                            onRegisterClick = { currentScreen = AppScreen.REGISTER }
                        )
                    }
                    
                    AppScreen.LOGIN -> {
                        LoginScreen(
                            database = database,
                            onBack = { currentScreen = AppScreen.LANDING },
                            onLoginSuccess = { email, role ->
                                currentUserEmail = email
                                userRole = role
                                
                                if (role == "MEDICO") {
                                    coroutineScope.launch {
                                        val utente = database.utenteDao().getUtenteByEmail(email)
                                        val medico = utente?.let { database.medicoDao().getMedicoByUtente(it.idUtente) }
                                        if (medico != null) {
                                            patientSurname = medico.cognome
                                            doctorPatients = database.pazienteDao().getPazientiByMedico(medico.idMedico)
                                            currentScreen = AppScreen.LOGGED_USER
                                        }
                                    }
                                } else if (role == "PAZIENTE") {
                                    // Pre-carichiamo i dati del paziente per i risultati
                                    coroutineScope.launch {
                                        val utente = database.utenteDao().getUtenteByEmail(email)
                                        val paziente = utente?.let { database.pazienteDao().getPazienteByUtente(it.idUtente) }
                                        
                                        if (paziente != null) {
                                            patientName = paziente.nome
                                            patientSurname = paziente.cognome
                                            patientBirthDate = paziente.dataNascita
                                            patientAge = com.example.progetto_7_vaccini.data.DateUtils.calculateAge(paziente.dataNascita)
                                            patientSex = paziente.sesso.toSex()
                                            
                                            val curaDb = database.curaBiologicaDao().getCura(paziente.idCura)
                                            patientBiologic = BiologicType.entries.find { it.label == curaDb?.nome } ?: BiologicType.TNF_INHIBITOR
                                            
                                            results = getVaccineRecommendations(patientSex!!, patientBiologic!!, patientAge, emptySet(), emptySet())
                                            currentScreen = AppScreen.LOGGED_USER
                                        }
                                    }
                                }
                            }
                        )
                    }
                    
                    AppScreen.REGISTER -> {
                        RegistrationScreen(
                            database = database,
                            onBack = { currentScreen = AppScreen.LANDING },
                            onRegisterSuccess = {
                                userRole = "PAZIENTE"
                                currentScreen = AppScreen.LOGIN 
                            }
                        )
                    }

                    AppScreen.FORM -> {
                        FormScreen(
                            initialName = patientName,
                            initialSurname = patientSurname,
                            initialBirthDate = patientBirthDate,
                            initialSex = patientSex,
                            initialBiologic = patientBiologic,
                            initialConditions = patientConditions,
                            initialHistory = patientHistory,
                            onBack = { currentScreen = AppScreen.LANDING },
                            onSubmit = { name, surname, birthDate, sex, biologic, conditions, history ->
                                patientName = name
                                patientSurname = surname
                                patientBirthDate = birthDate
                                val age = com.example.progetto_7_vaccini.data.DateUtils.calculateAge(birthDate)
                                patientAge = age
                                patientSex = sex
                                patientBiologic = biologic
                                patientConditions = conditions
                                patientHistory = history
                                results = getVaccineRecommendations(sex, biologic, age, conditions, history)
                                currentScreen = AppScreen.RESULTS
                            }
                        )
                    }
                    
                    AppScreen.RESULTS -> {
                        ResultsScreen(
                            patientName    = patientName,
                            patientSurname = patientSurname,
                            patientAge     = patientAge,
                            sex            = patientSex!!,
                            biologic       = patientBiologic!!,
                            recommendations = results!!,
                            onBack = { 
                                if (userRole == "MEDICO") {
                                    currentScreen = AppScreen.LOGGED_USER
                                } else {
                                    // Guest torna al form
                                    currentScreen = AppScreen.FORM
                                }
                            }
                        )
                    }

                    AppScreen.MODIFY_DATA -> {
                        ModifyDataScreen(
                            initialName = patientName,
                            initialSurname = patientSurname,
                            initialBirthDate = patientBirthDate,
                            initialSex = patientSex,
                            initialBiologic = patientBiologic,
                            initialConditions = patientConditions,
                            initialHistory = patientHistory,
                            initialEmail = currentUserEmail ?: "",
                            onBack = { currentScreen = AppScreen.LOGGED_USER },
                            onEmailUpdate = { nuovaEmail, callback ->
                                coroutineScope.launch {
                                    val utenteEsistente = database.utenteDao().getUtenteByEmail(nuovaEmail)
                                    if (utenteEsistente == null || nuovaEmail == currentUserEmail) {
                                        currentUserEmail?.let { vecchiaEmail ->
                                            database.utenteDao().aggiornaEmailByEmail(vecchiaEmail, nuovaEmail)
                                            currentUserEmail = nuovaEmail
                                        }
                                        callback(null)
                                    } else {
                                        callback("Email non valida")
                                    }
                                }
                            },
                            onConfirm = { name, surname, birthDate, sex, biologic, conditions, history ->
                                patientName = name
                                patientSurname = surname
                                patientBirthDate = birthDate
                                val age = com.example.progetto_7_vaccini.data.DateUtils.calculateAge(birthDate)
                                patientAge = age
                                patientSex = sex
                                patientBiologic = biologic
                                patientConditions = conditions
                                patientHistory = history
                                results = getVaccineRecommendations(sex, biologic, age, conditions, history)
                                
                                // Dopo la modifica, ricalcoliamo e torniamo alla Home loggata (che mostra i risultati aggiornati)
                                currentScreen = AppScreen.LOGGED_USER
                            }
                        )
                    }

                    AppScreen.LOGGED_USER -> {
                        LoggedUserScreen(
                            userRole = userRole,
                            userEmail = currentUserEmail,
                            patientName = patientName,
                            patientSurname = patientSurname,
                            patientAge = patientAge,
                            patientSex = patientSex,
                            patientBiologic = patientBiologic,
                            recommendations = results,
                            doctorPatients = doctorPatients,
                            onLogout = { currentScreen = AppScreen.LANDING },
                            onModificaDati = { currentScreen = AppScreen.MODIFY_DATA },
                            onChangePassword = { 
                                coroutineScope.launch {
                                    currentUserEmail?.let { email ->
                                        val utente = database.utenteDao().getUtenteByEmail(email)
                                        actualPassword = utente?.password ?: ""
                                    }
                                    previousScreen = AppScreen.LOGGED_USER
                                    currentScreen = AppScreen.NEW_PASSWORD 
                                }
                            },
                            onPatientClick = { paziente ->
                                coroutineScope.launch {
                                    patientName = paziente.nome
                                    patientSurname = paziente.cognome
                                    patientBirthDate = paziente.dataNascita
                                    patientAge = com.example.progetto_7_vaccini.data.DateUtils.calculateAge(paziente.dataNascita)
                                    patientSex = paziente.sesso.toSex()
                                    
                                    val curaDb = database.curaBiologicaDao().getCura(paziente.idCura)
                                    patientBiologic = BiologicType.entries.find { it.label == curaDb?.nome } ?: BiologicType.TNF_INHIBITOR
                                    
                                    // Qui andrebbero caricate anche le condizioni e la storia se salvate nel DB
                                    results = getVaccineRecommendations(patientSex!!, patientBiologic!!, patientAge, emptySet(), emptySet())
                                    
                                    previousScreen = AppScreen.LOGGED_USER
                                    currentScreen = AppScreen.RESULTS
                                }
                            }
                        )
                    }

                    AppScreen.NEW_PASSWORD -> {
                        NewPasswordScreen(
                            currentActualPassword = actualPassword,
                            onBack = { currentScreen = previousScreen },
                            onConfirm = { newPassword ->
                                coroutineScope.launch {
                                    currentUserEmail?.let { email ->
                                        database.utenteDao().aggiornaPassword(email, newPassword)
                                    }
                                    currentScreen = previousScreen
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
