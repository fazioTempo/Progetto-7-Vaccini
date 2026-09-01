package com.example.progetto_7_vaccini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.progetto_7_vaccini.data.DateUtils
import com.example.progetto_7_vaccini.data.database.DatabaseInitializer
import com.example.progetto_7_vaccini.data.database.DatabaseProvider
import com.example.progetto_7_vaccini.data.database.entities.*
import com.example.progetto_7_vaccini.data.models.VaccineRec
import com.example.progetto_7_vaccini.screens.*
import com.example.progetto_7_vaccini.ui.MainViewModel
import com.example.progetto_7_vaccini.ui.theme.VaccineBiologicTheme
import kotlinx.coroutines.launch

enum class AppScreen {
    LANDING, LOGIN, REGISTER, FORM, RESULTS, NEW_PASSWORD, LOGGED_USER, MODIFY_DATA
}

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(DatabaseProvider.getDatabase(applicationContext)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var isDbInitialized by mutableStateOf(false)
        val database = DatabaseProvider.getDatabase(applicationContext)
        lifecycleScope.launch {
            DatabaseInitializer.inizializza(database)
            isDbInitialized = true
        }
        setContent {
            VaccineBiologicTheme {
                // Navigation state
                var currentScreen by rememberSaveable { mutableStateOf(AppScreen.LANDING) }
                var previousScreen by rememberSaveable { mutableStateOf(AppScreen.LANDING) }
                var userRole by rememberSaveable { mutableStateOf<String?>(null) } 
                var currentUserEmail by rememberSaveable { mutableStateOf<String?>(null) }
                var actualPassword by rememberSaveable { mutableStateOf("") }
                var currentPatientId by rememberSaveable { mutableStateOf<Long?>(null) }

                // Observe ViewModel
                val results by viewModel.recommendations.collectAsState()

                // Options from DB
                var dbBiologicOptions by remember { mutableStateOf<List<CuraBiologica>>(emptyList()) }
                var dbConditionOptions by remember { mutableStateOf<List<CondizioneClinica>>(emptyList()) }
                var dbVaccineOptions by remember { mutableStateOf<List<Vaccino>>(emptyList()) }

                LaunchedEffect(isDbInitialized) {
                    if (isDbInitialized) {
                        dbBiologicOptions = database.curaBiologicaDao().getTutteLeCure()
                        dbConditionOptions = database.condizioneClinicaDao().getTutteLeCondizioni()
                        dbVaccineOptions = database.vaccinoDao().getTuttiVaccini()
                    }
                }

                val coroutineScope = rememberCoroutineScope()
                
                // Form data state
                var patientName by rememberSaveable { mutableStateOf("") }
                var patientSurname by rememberSaveable { mutableStateOf("") }
                var patientBirthDate by rememberSaveable { mutableStateOf("") }
                var patientAge by rememberSaveable { mutableStateOf<Int?>(null) }
                var patientSex by rememberSaveable { mutableStateOf<Sesso?>(null) }
                var patientBiologic by rememberSaveable { mutableStateOf<CuraBiologica?>(null) }
                var patientConditions by rememberSaveable { mutableStateOf(setOf<Long>()) }
                var patientHistory by rememberSaveable { mutableStateOf(setOf<Long>()) }
                var doctorPatients by rememberSaveable { mutableStateOf<List<Paziente>>(emptyList()) }

                @OptIn(ExperimentalMaterial3Api::class)
                when (currentScreen) {
                    AppScreen.LANDING -> {
                        LandingScreen(
                            onGuestClick = {
                                viewModel.clearRecommendations()
                                patientName = ""
                                patientSurname = ""
                                patientBirthDate = ""
                                patientAge = null
                                patientSex = null
                                patientBiologic = null
                                patientConditions = emptySet()
                                patientHistory = emptySet()

                                userRole = null
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
                                    coroutineScope.launch {
                                        val utente = database.utenteDao().getUtenteByEmail(email)
                                        val paziente = utente?.let { database.pazienteDao().getPazienteByUtente(it.idUtente) }

                                        if (paziente != null) {
                                            currentPatientId = paziente.idPaziente
                                            patientName = paziente.nome
                                            patientSurname = paziente.cognome
                                            patientBirthDate = paziente.dataNascita
                                            patientAge = DateUtils.calculateAge(paziente.dataNascita)
                                            patientSex = paziente.sesso

                                            patientBiologic = database.curaBiologicaDao().getCura(paziente.idCura)

                                            val condPaziente = database.pazienteCondizioneDao().getCondizioniByPaziente(paziente.idPaziente)
                                            patientConditions = condPaziente.map { it.idCondizione }.toSet()

                                            val vaccPaziente = database.vaccinazioneDao().getVaccinazioniByPaziente(paziente.idPaziente)
                                            patientHistory = vaccPaziente.map { it.idVaccino }.toSet()

                                            viewModel.loadRecommendations(paziente.idPaziente)
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
                            biologicOptions = dbBiologicOptions,
                            conditionOptions = dbConditionOptions,
                            vaccineOptions = dbVaccineOptions,
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
                            biologicOptions = dbBiologicOptions,
                            conditionOptions = dbConditionOptions,
                            vaccineOptions = dbVaccineOptions,
                            onBack = { currentScreen = AppScreen.LANDING },
                            onSubmit = { name, surname, birthDate, sex, biologic, conditions, history ->
                                patientName = name
                                patientSurname = surname
                                patientBirthDate = birthDate
                                val age = DateUtils.calculateAge(birthDate)
                                patientAge = age
                                patientSex = sex
                                patientBiologic = biologic
                                patientConditions = conditions
                                patientHistory = history

                                viewModel.calculateGuest(
                                    sex = if (sex == Sesso.MASCHIO) "Maschio" else "Femmina",
                                    biologic = biologic,
                                    age = age,
                                    conditions = dbConditionOptions.filter { it.idCondizione in conditions },
                                    history = history,
                                    vaccineOptions = dbVaccineOptions
                                )
                                currentScreen = AppScreen.RESULTS
                            }
                        )
                    }

                    AppScreen.RESULTS -> {
                        ResultsScreen(
                            patientName    = patientName,
                            patientSurname = patientSurname,
                            patientAge     = patientAge,
                            sex            = patientSex ?: Sesso.MASCHIO,
                            biologic       = patientBiologic!!,
                            recommendations = results,
                            onBack = {
                                if (userRole == "MEDICO") {
                                    currentScreen = AppScreen.LOGGED_USER
                                } else {
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
                            biologicOptions = dbBiologicOptions,
                            conditionOptions = dbConditionOptions,
                            vaccineOptions = dbVaccineOptions,
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
                                val age = DateUtils.calculateAge(birthDate)
                                patientAge = age
                                patientSex = sex
                                patientBiologic = biologic
                                patientConditions = conditions
                                patientHistory = history

                                coroutineScope.launch {
                                    currentPatientId?.let { id ->
                                        val p = database.pazienteDao().getPaziente(id)
                                        if (p != null) {
                                            database.pazienteDao().inserisciPaziente(
                                                p.copy(
                                                    nome = name,
                                                    cognome = surname,
                                                    dataNascita = birthDate,
                                                    sesso = sex,
                                                    idCura = biologic.idCura
                                                )
                                            )
                                        }

                                        database.pazienteCondizioneDao().cancellaTutteCondizioniPerPaziente(id)
                                        conditions.forEach { condId ->
                                            database.pazienteCondizioneDao().inserisciPazienteCondizione(
                                                PazienteCondizione(idPaziente = id, idCondizione = condId)
                                            )
                                        }

                                        database.vaccinazioneDao().cancellaTutteVaccinazioniPerPaziente(id)
                                        history.forEach { vaccinoId ->
                                            database.vaccinazioneDao().inserisciVaccinazione(
                                                Vaccinazione(
                                                    idPaziente = id,
                                                    idVaccino = vaccinoId
                                                )
                                            )
                                        }

                                        viewModel.loadRecommendations(id, forceRefresh = true)
                                    }
                                }

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
                            recommendations = results.takeIf { it.isNotEmpty() },
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
                                    currentPatientId = paziente.idPaziente
                                    patientName = paziente.nome
                                    patientSurname = paziente.cognome
                                    patientBirthDate = paziente.dataNascita
                                    patientAge = DateUtils.calculateAge(paziente.dataNascita)
                                    patientSex = paziente.sesso

                                    patientBiologic = database.curaBiologicaDao().getCura(paziente.idCura)

                                    val condPaziente = database.pazienteCondizioneDao().getCondizioniByPaziente(paziente.idPaziente)
                                    patientConditions = condPaziente.map { it.idCondizione }.toSet()

                                    val vaccPaziente = database.vaccinazioneDao().getVaccinazioniByPaziente(paziente.idPaziente)
                                    patientHistory = vaccPaziente.map { it.idVaccino }.toSet()

                                    viewModel.loadRecommendations(paziente.idPaziente)

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
