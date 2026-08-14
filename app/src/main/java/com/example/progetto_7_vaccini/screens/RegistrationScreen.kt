package com.example.progetto_7_vaccini.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.progetto_7_vaccini.data.BiologicType
import com.example.progetto_7_vaccini.data.MedicalCondition
import com.example.progetto_7_vaccini.data.Sex
import com.example.progetto_7_vaccini.data.database.AppDatabase
import com.example.progetto_7_vaccini.data.database.entities.Medico
import com.example.progetto_7_vaccini.data.database.entities.Paziente
import com.example.progetto_7_vaccini.data.database.entities.Utente
import com.example.progetto_7_vaccini.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    database: AppDatabase,
    onBack: () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    // ── Credenziali ──────────────────────────────────────────────────────────
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    // ── Dati Paziente (Riuso VaccineFormContent) ────────────────────────────
    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("") }
    var ageStr by rememberSaveable { mutableStateOf("") }
    var sex by rememberSaveable { mutableStateOf<Sex?>(null) }
    var biologic by rememberSaveable { mutableStateOf<BiologicType?>(null) }
    val conditions = rememberSaveable { mutableStateOf(setOf<MedicalCondition>()) }
    val history = rememberSaveable { mutableStateOf(setOf<String>()) }

    // ── Medico Scelto ────────────────────────────────────────────────────────
    var doctorList by remember { mutableStateOf<List<Medico>>(emptyList()) }
    var selectedDoctor by remember { mutableStateOf<Medico?>(null) }
    var doctorExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        doctorList = database.medicoDao().getTuttiIMedici()
        if (doctorList.isNotEmpty()) {
            selectedDoctor = doctorList[0]
        }
    }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

            val isFormValid = email.isNotBlank() && 
            password.isNotBlank() && 
            password == confirmPassword &&
            name.isNotBlank() && 
            surname.isNotBlank() &&
            sex != null && 
            biologic != null &&
            selectedDoctor != null

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
                        text = "REGISTRAZIONE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Teal100,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Crea il tuo profilo paziente",
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
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Completa tutti i campi per registrarti e accedere ai tuoi dati sanitari.",
                style = MaterialTheme.typography.bodySmall,
                color = Slate600
            )

            // ── Credenziali Section ──────────────────────────────────────────
            SectionLabel("Dati di accesso")
            
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = outlinedFieldColors()
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = outlinedFieldColors()
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Conferma Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = outlinedFieldColors()
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Slate200)

            // ── Medico Curante ───────────────────────────────────────────────
            SectionLabel("Medico Curante")
            ExposedDropdownMenuBox(
                expanded = doctorExpanded,
                onExpandedChange = { doctorExpanded = !doctorExpanded }
            ) {
                OutlinedTextField(
                    value = selectedDoctor?.let { "${it.nome} ${it.cognome}" } ?: "Caricamento medici...",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Seleziona il tuo medico", color = Slate400) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = doctorExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = outlinedFieldColors()
                )
                ExposedDropdownMenu(
                    expanded = doctorExpanded,
                    onDismissRequest = { doctorExpanded = false }
                ) {
                    doctorList.forEach { medico ->
                        DropdownMenuItem(
                            text = { Text("${medico.nome} ${medico.cognome}") },
                            onClick = {
                                selectedDoctor = medico
                                doctorExpanded = false
                            }
                        )
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Slate200)

            // ── Dati Personali (VaccineFormContent) ─────────────────────────
            Text(
                text = "DATI SANITARI",
                style = MaterialTheme.typography.labelSmall,
                color = Teal700,
                fontWeight = FontWeight.Bold
            )

            VaccineFormContent(
                name = name,
                onNameChange = { name = it },
                surname = surname,
                onSurnameChange = { surname = it },
                ageStr = ageStr,
                onAgeChange = { ageStr = it },
                sex = sex,
                onSexChange = { sex = it },
                biologic = biologic,
                onBiologicChange = { biologic = it },
                conditions = conditions.value,
                onConditionsChange = { conditions.value = it },
                history = history.value,
                onHistoryChange = { history.value = it }
            )

            // ── Register Button ──────────────────────────────────────────────
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    if (isFormValid) {
                        coroutineScope.launch {
                            // 1. Salvataggio Utente
                            val idUtente = database.utenteDao().inserisciUtente(
                                Utente(
                                    email = email,
                                    password = password,
                                    ruolo = "PAZIENTE"
                                )
                            )
                            
                            // 2. Recupero ID della cura biologica selezionata
                            val tutteLeCure = database.curaBiologicaDao().getTutteLeCure()
                            val idCuraSelezionata = tutteLeCure.find { it.nome == biologic!!.label }?.idCura ?: 1L
                            
                            // 3. Salvataggio Paziente
                            database.pazienteDao().inserisciPaziente(
                                Paziente(
                                    idUtente = idUtente,
                                    idMedico = selectedDoctor?.idMedico ?: 1L, 
                                    idCura = idCuraSelezionata,
                                    nome = name,
                                    cognome = surname,
                                    dataNascita = "Età: $ageStr", // Usiamo l'età per ora nel campo data
                                    sesso = sex!!.label.take(1) // M o F
                                )
                            )
                            
                            onRegisterSuccess()
                        }
                    }
                },
                enabled = isFormValid,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Teal900,
                    contentColor = Color.White,
                    disabledContainerColor = Slate200,
                    disabledContentColor = Slate400
                )
            ) {
                Text(
                    text = "REGISTRATI ORA",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            
            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── Helper (copiato da FormScreen per ora o reso public lì) ───────────────────
@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        color = Slate600,
        modifier = Modifier.padding(bottom = 2.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Teal700,
    unfocusedBorderColor = Slate200,
    focusedLabelColor = Teal700,
    cursorColor = Teal700
)
