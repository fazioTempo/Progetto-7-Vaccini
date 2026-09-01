package com.example.progetto_7_vaccini.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.progetto_7_vaccini.data.database.AppDatabase
import com.example.progetto_7_vaccini.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    database: AppDatabase,
    onBack: () -> Unit,
    onLoginSuccess: (email: String, role: String) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(Teal900)
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Indietro",
                        tint = Color.White
                    )
                }
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Text(
                        text = "ACCESSO",
                        style = MaterialTheme.typography.labelSmall,
                        color = Teal100,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Bentornato",
                        style = MaterialTheme.typography.displayMedium,
                        color = Color.White,
                        lineHeight = 32.sp
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { 
                    email = it
                    errorMessage = null
                },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Teal700,
                    unfocusedBorderColor = Slate200,
                    focusedLabelColor = Teal700
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = { 
                    password = it
                    errorMessage = null
                },
                label = { Text("Password") },
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
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Teal700,
                    unfocusedBorderColor = Slate200,
                    focusedLabelColor = Teal700
                )
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (email.isNotBlank() && password.isNotBlank()) {
                        coroutineScope.launch {
                            val user = database.utenteDao().getUtenteByEmail(email)
                            if (user != null && user.password == password) {
                                onLoginSuccess(user.email, user.ruolo)
                            } else {
                                errorMessage = "Email o password non corretti"
                            }
                        }
                    } else {
                        errorMessage = "Inserisci tutte le credenziali"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Teal900,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "ACCEDI",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
