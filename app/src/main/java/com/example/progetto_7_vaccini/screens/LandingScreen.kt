package com.example.progetto_7_vaccini.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.progetto_7_vaccini.ui.theme.*

@Composable
fun LandingScreen(
    onGuestClick: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // ── Logo / Icon Placeholder ──────────────────────────────────────
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(Teal100, RoundedCornerShape(30.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Vaccines,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Teal900
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── App Name ─────────────────────────────────────────────────────
            Text(
                text = "VaxAdvisor", // Placeholder per il nome dell'app
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Teal900
                ),
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Supporto clinico alle vaccinazioni in terapia biologica",
                style = MaterialTheme.typography.bodyMedium,
                color = Slate600,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp).padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(64.dp))

            // ── Buttons ──────────────────────────────────────────────────────
            
            // 1. GUEST BUTTON (Accesso Rapido)
            Button(
                onClick = onGuestClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Teal600,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "ACCESSO OSPITE",
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. LOGIN BUTTON
            OutlinedButton(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Teal900
                )
            ) {
                Text(
                    text = "ACCEDI",
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. REGISTER BUTTON
            TextButton(
                onClick = onRegisterClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Non hai un account? Registrati ora",
                    color = Slate600,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
