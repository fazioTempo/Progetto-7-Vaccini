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
import com.example.progetto_7_vaccini.data.getVaccineRecommendations
import com.example.progetto_7_vaccini.screens.FormScreen
import com.example.progetto_7_vaccini.screens.ResultsScreen
import com.example.progetto_7_vaccini.ui.theme.VaccineBiologicTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VaccineBiologicTheme {
                // Simple two-screen navigation via state (no NavHost needed for this scope)
                var results by rememberSaveable { mutableStateOf<List<VaccineRec>?>(null) }
                var patientName by rememberSaveable { mutableStateOf("") }
                var patientAge by rememberSaveable { mutableStateOf<Int?>(null) }
                var patientSex by rememberSaveable { mutableStateOf<Sex?>(null) }
                var patientBiologic by rememberSaveable { mutableStateOf<BiologicType?>(null) }

                if (results == null) {
                    FormScreen(
                        onSubmit = { name, age, sex, biologic, conditions, history ->
                            patientName = name
                            patientAge = age
                            patientSex = sex
                            patientBiologic = biologic
                            results = getVaccineRecommendations(sex, biologic, age, conditions, history)
                        }
                    )
                } else {
                    ResultsScreen(
                        patientName    = patientName,
                        patientAge     = patientAge,
                        sex            = patientSex!!,
                        biologic       = patientBiologic!!,
                        recommendations = results!!,
                        onBack = { results = null }
                    )
                }
            }
        }
    }
}