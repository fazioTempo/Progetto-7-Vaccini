package com.example.progetto_7_vaccini.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
    // Se l'utente è un guest (userRole == null), non mostriamo nulla
    if (userRole == null) return

    var expanded by remember { mutableStateOf(false) }

    // Logica di visibilità basata sul ruolo
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
            // Il Paziente vede MODIFICA DATI. Il Medico no.
            if (isPaziente) {
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

            // Sia Paziente che Medico vedono CAMBIA PASSWORD e LOGOUT.
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
