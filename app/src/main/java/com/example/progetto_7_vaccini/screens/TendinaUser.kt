package com.example.progetto_7_vaccini.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.progetto_7_vaccini.ui.theme.VaccineBiologicTheme

/**
 * Composable che mostra un'icona profilo cliccabile.
 * Al clic apre un menu a tendina con le opzioni "MODIFICA" e "LOGOUT".
 */
@Composable
fun TendinaUser(
    onModifica: () -> Unit,
    onChangePassword: () -> Unit,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    userRole: String? = null // null = Ospite, "PAZIENTE" = Paziente, "MEDICO" = Medico
) {
    var expanded by remember { mutableStateOf(false) }

    // Logica di visibilità basata sul ruolo
    val isGuest = userRole == null
    val isPaziente = userRole == "PAZIENTE"
    val isMedico = userRole == "MEDICO"

    Box(modifier = modifier) {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Menu Profilo",
                modifier = Modifier.size(40.dp),
                tint = Color.Gray
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            // L'Ospite e il Paziente vedono MODIFICA DATI. Il Medico no.
            if (isGuest || isPaziente) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "MODIFICA DATI",
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    onClick = {
                        expanded = false
                        onModifica()
                    }
                )
            }

            // Solo Paziente e Medico vedono CAMBIA PASSWORD e LOGOUT. L'Ospite no.
            if (isPaziente || isMedico) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "CAMBIA PASSWORD",
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    onClick = {
                        expanded = false
                        onChangePassword()
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "LOGOUT",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    },
                    onClick = {
                        expanded = false
                        onLogout()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Menu Paziente")
@Composable
fun TendinaUserRegPreview() {
    VaccineBiologicTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
                .border(1.dp, Color.LightGray)
        ) {
            DropdownMenuItem(
                text = { Text("MODIFICA DATI", style = MaterialTheme.typography.labelLarge) },
                onClick = {}
            )
            DropdownMenuItem(
                text = { Text("CAMBIA PASSWORD", style = MaterialTheme.typography.labelLarge) },
                onClick = {}
            )
            DropdownMenuItem(
                text = { Text("LOGOUT", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.error) },
                onClick = {}
            )
        }
    }
}
