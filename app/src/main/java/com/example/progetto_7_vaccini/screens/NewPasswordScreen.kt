package com.example.progetto_7_vaccini.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.progetto_7_vaccini.data.ValidationUtils

/**
 * Schermata per l'inserimento di una nuova password.
 * Include due campi di testo per la verifica e pulsanti di conferma/annullamento.
 */
@Composable
fun NewPasswordScreen(
    currentActualPassword: String,
    onBack: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var currentPasswordInput by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var currentPasswordVisible by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = Color.White
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            // Zona centrale per i campi di testo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.Start
            ) {
                // ZONA PASSWORD ATTUALE
                Text(
                    text = "INSERISCI PASSWORD ATTUALE",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = currentPasswordInput,
                    onValueChange = {
                        currentPasswordInput = it
                        errorMessage = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (currentPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (currentPasswordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description = if (currentPasswordVisible) "Nascondi password" else "Mostra password"

                        IconButton(onClick = { currentPasswordVisible = !currentPasswordVisible }) {
                            Icon(imageVector = image, contentDescription = description)
                        }
                    },
                    singleLine = true,
                    placeholder = { Text("Password Attuale", color = Color.Gray) }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // PRIMA ZONA (Centro-Alto) - Password Nuova
                Text(
                    text = "INSERISCI NUOVA PASSWORD",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { 
                        password = it
                        errorMessage = null 
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description = if (passwordVisible) "Nascondi password" else "Mostra password"

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = description)
                        }
                    },
                    singleLine = true,
                    placeholder = { Text("Password", color = Color.Gray) }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // SECONDA ZONA (Centro-Basso) - Conferma Password Nuova
                Text(
                    text = "INSERISCI NUOVAMENTE LA NUOVA PASSWORD",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { 
                        confirmPassword = it
                        errorMessage = null 
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (confirmPasswordVisible)
                            Icons.Filled.Visibility
                        else Icons.Filled.VisibilityOff

                        val description = if (confirmPasswordVisible) "Nascondi password" else "Mostra password"

                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(imageVector = image, contentDescription = description)
                        }
                    },
                    singleLine = true,
                    placeholder = { Text("Ripeti Password", color = Color.Gray) },
                    isError = errorMessage != null
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            // Pulsanti in basso
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray)
                ) {
                    Text("ANNULLA", color = Color.Black)
                }
                Button(
                    onClick = {
                        if (currentPasswordInput != currentActualPassword) {
                            errorMessage = "La password attuale non è corretta"
                        } else if (!ValidationUtils.isValidPassword(password)) {
                            errorMessage = "La nuova password deve contenere almeno 10 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale"
                        } else if (password == confirmPassword) {
                            onConfirm(password)
                        } else {
                            errorMessage = "Le nuove password non coincidono"
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("CONFERMA", color = Color.White)
                }
            }
        }
    }
}
