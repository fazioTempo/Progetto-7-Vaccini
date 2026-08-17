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

enum class AppScreen {
    LANDING, LOGIN, REGISTER, FORM, RESULTS, NEW_PASSWORD, MEDICO_AREA
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
                var patientAge by rememberSaveable { mutableStateOf<Int?>(null) }
                var patientSex by rememberSaveable { mutableStateOf<Sex?>(null) }
                var patientBiologic by rememberSaveable { mutableStateOf<BiologicType?>(null) }
                var patientConditions by rememberSaveable { mutableStateOf(setOf<MedicalCondition>()) }
                var patientHistory by rememberSaveable { mutableStateOf(setOf<String>()) }
                var isEditingPatient by rememberSaveable { mutableStateOf(false) }

                @OptIn(ExperimentalMaterial3Api::class)
                when (currentScreen) {
                    AppScreen.LANDING -> {
                        LandingScreen(
                            onGuestClick = { 
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
                                    currentScreen = AppScreen.MEDICO_AREA
                                } else {
                                    // Se è un paziente, carichiamo i dati e andiamo ai risultati
                                    coroutineScope.launch {
                                        val utente = database.utenteDao().getUtenteByEmail(email)
                                        val paziente = utente?.let { database.pazienteDao().getPazienteByUtente(it.idUtente) }
                                        
                                        if (paziente != null) {
                                            patientName = paziente.nome
                                            patientSurname = paziente.cognome
                                            patientAge = paziente.dataNascita.replace("Età: ", "").toIntOrNull()
                                            patientSex = if (paziente.sesso == Sesso.MASCHIO) Sex.MALE else Sex.FEMALE
                                            
                                            // Recupero la cura dal DB per mappare l'enum BiologicType
                                            val curaDb = database.curaBiologicaDao().getCura(paziente.idCura)
                                            patientBiologic = BiologicType.entries.find { it.label == curaDb?.nome } ?: BiologicType.TNF_INHIBITOR
                                            
                                            // Calcolo le raccomandazioni (vuote per ora le condizioni/storia, andrebbero caricate anche quelle se presenti)
                                            results = getVaccineRecommendations(patientSex!!, patientBiologic!!, patientAge, emptySet(), emptySet())
                                            
                                            currentScreen = AppScreen.RESULTS
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
                            initialName = if (isEditingPatient) patientName else "",
                            initialSurname = if (isEditingPatient) patientSurname else "",
                            initialAge = if (isEditingPatient) (patientAge?.toString() ?: "") else "",
                            initialSex = if (isEditingPatient) patientSex else null,
                            initialBiologic = if (isEditingPatient) patientBiologic else null,
                            initialConditions = if (isEditingPatient) patientConditions else emptySet(),
                            initialHistory = if (isEditingPatient) patientHistory else emptySet(),
                            initialEmail = currentUserEmail ?: "",
                            isEditing = isEditingPatient,
                            onBack = { 
                                if (isEditingPatient) currentScreen = AppScreen.RESULTS
                                else currentScreen = AppScreen.LANDING 
                            },
                            onEmailUpdate = { nuovaEmail, callback ->
                                coroutineScope.launch {
                                    val utenteEsistente = database.utenteDao().getUtenteByEmail(nuovaEmail)
                                    if (utenteEsistente == null || nuovaEmail == currentUserEmail) {
                                        currentUserEmail?.let { vecchiaEmail ->
                                            database.utenteDao().aggiornaEmailByEmail(vecchiaEmail, nuovaEmail)
                                            currentUserEmail = nuovaEmail
                                        }
                                        callback(null) // Successo
                                    } else {
                                        callback("Email non valida")
                                    }
                                }
                            },
                            onSubmit = { name, surname, age, sex, biologic, conditions, history ->
                                patientName = name
                                patientSurname = surname
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
                                isEditingPatient = true
                                currentScreen = AppScreen.FORM 
                            },
                            onModificaDati = { 
                                isEditingPatient = true
                                currentScreen = AppScreen.FORM 
                            },
                            onChangePassword = { 
                                coroutineScope.launch {
                                    currentUserEmail?.let { email ->
                                        val utente = database.utenteDao().getUtenteByEmail(email)
                                        actualPassword = utente?.password ?: ""
                                    }
                                    previousScreen = AppScreen.RESULTS
                                    currentScreen = AppScreen.NEW_PASSWORD 
                                }
                            },
                            onLogout = { currentScreen = AppScreen.LANDING },
                            userRole = userRole
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

                    AppScreen.MEDICO_AREA -> {
                        // Schermata placeholder per l'area medico (lista pazienti)
                        Scaffold(
                            topBar = {
                                CenterAlignedTopAppBar(
                                    title = { Text("Area Medico") },
                                    actions = {
                                        IconButton(onClick = { currentScreen = AppScreen.LANDING }) {
                                            Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                                        }
                                    }
                                )
                            }
                        ) { padding ->
                            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                                Text("Benvenuto Dottore!\nLa lista pazienti sarà disponibile a breve.", textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }
    }
}
