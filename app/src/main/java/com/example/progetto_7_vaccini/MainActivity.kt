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

enum class AppScreen {
    LANDING, LOGIN, REGISTER, FORM, RESULTS, NEW_PASSWORD
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

                val coroutineScope = rememberCoroutineScope()
                
                // Form data state
                var results by rememberSaveable { mutableStateOf<List<VaccineRec>?>(null) }
                var patientName by rememberSaveable { mutableStateOf("") }
                var patientSurname by rememberSaveable { mutableStateOf("") }
                var patientAge by rememberSaveable { mutableStateOf<Int?>(null) }
                var patientSex by rememberSaveable { mutableStateOf<Sex?>(null) }
                var patientBiologic by rememberSaveable { mutableStateOf<BiologicType?>(null) }

                when (currentScreen) {
                    AppScreen.LANDING -> {
                        LandingScreen(
                            onGuestClick = { 
                                userRole = null
                                currentScreen = AppScreen.FORM 
                            },
                            onLoginClick = { currentScreen = AppScreen.LOGIN },
                            onRegisterClick = { 
                                previousScreen = AppScreen.LANDING
                                currentScreen = AppScreen.REGISTER 
                            }
                        )
                    }
                    
                    AppScreen.LOGIN -> {
                        LoginScreen(onBack = { currentScreen = AppScreen.LANDING })
                    }
                    
                    AppScreen.REGISTER -> {
                        RegistrationScreen(
                            database = database,
                            onBack = { currentScreen = previousScreen },
                            onRegisterSuccess = { email ->
                                userRole = "PAZIENTE"
                                currentUserEmail = email
                                currentScreen = AppScreen.LOGIN 
                            }
                        )
                    }

                    AppScreen.FORM -> {
                        FormScreen(
                            onBack = { currentScreen = AppScreen.LANDING },
                            onSubmit = { name, surname, age, sex, biologic, conditions, history ->
                                patientName = name
                                patientSurname = surname
                                patientAge = age
                                patientSex = sex
                                patientBiologic = biologic
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
                            onBack = { currentScreen = AppScreen.FORM },
                            onModificaDati = { currentScreen = AppScreen.FORM },
                            onChangePassword = { 
                                previousScreen = AppScreen.RESULTS
                                currentScreen = AppScreen.NEW_PASSWORD 
                            },
                            onLogout = { currentScreen = AppScreen.LANDING },
                            userRole = userRole
                        )
                    }

                    AppScreen.NEW_PASSWORD -> {
                        NewPasswordScreen(
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
