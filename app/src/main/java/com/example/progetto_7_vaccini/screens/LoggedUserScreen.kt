package com.example.progetto_7_vaccini.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.ui.text.font.FontWeight
import com.example.progetto_7_vaccini.data.models.VaccineRec
import com.example.progetto_7_vaccini.data.database.entities.Paziente
import com.example.progetto_7_vaccini.data.database.entities.CuraBiologica
import com.example.progetto_7_vaccini.data.database.entities.Sesso

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoggedUserScreen(
    userRole: String?,
    userEmail: String?,
    patientName: String,
    patientSurname: String,
    patientAge: Int?,
    patientSex: Sesso?,
    patientBiologic: CuraBiologica?,
    recommendations: List<VaccineRec>?,
    doctorPatients: List<Paziente> = emptyList(),
    onLogout: () -> Unit,
    onModificaDati: () -> Unit,
    onChangePassword: () -> Unit,
    onPatientClick: (Paziente) -> Unit = {}
) {
    val welcomeTitle = if (userRole == "MEDICO") {
        "Salve Dr. $patientSurname"
    } else {
        if (patientSex == Sesso.FEMMINA) "Bentornata $patientName" else "Bentornato $patientName"
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(welcomeTitle) 
                },
                actions = {
                    UserDropdownMenu(
                        onModifica = onModificaDati,
                        onChangePassword = onChangePassword,
                        onLogout = onLogout,
                        userRole = userRole
                    )
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            if (userRole == "MEDICO") {
                if (doctorPatients.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Non ci sono ancora pazienti associati.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            Text(
                                text = "I TUOI PAZIENTI",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        items(doctorPatients) { paziente ->
                            Card(
                                onClick = { onPatientClick(paziente) },
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${paziente.nome} ${paziente.cognome}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Data di nascita: ${paziente.dataNascita}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Visualizza dettagli",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                if (recommendations != null && patientSex != null && patientBiologic != null) {
                    ResultsContent(
                        patientName = patientName,
                        patientSurname = patientSurname,
                        patientAge = patientAge,
                        sex = if (patientSex == Sesso.MASCHIO) "Maschio" else "Femmina",
                        biologic = patientBiologic,
                        recommendations = recommendations
                    )
                } else {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
