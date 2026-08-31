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
import com.example.progetto_7_vaccini.data.DateUtils
import com.example.progetto_7_vaccini.data.ValidationUtils
import com.example.progetto_7_vaccini.data.database.AppDatabase
import com.example.progetto_7_vaccini.data.database.entities.*
import com.example.progetto_7_vaccini.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    database: AppDatabase,
    biologicOptions: List<CuraBiologica> = emptyList(),
    conditionOptions: List<CondizioneClinica> = emptyList(),
    vaccineOptions: List<Vaccino> = emptyList(),
    onBack: () -> Unit,
    onRegisterSuccess: (String) -> Unit
) {
    // ── Credenziali ──────────────────────────────────────────────────────────
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    // ── Dati Paziente (Riuso VaccineFormContent) ────────────────────────────
    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    var sex by rememberSaveable { mutableStateOf<String?>(null) }
    var biologic by rememberSaveable { mutableStateOf<CuraBiologica?>(null) }
    val conditions = rememberSaveable { mutableStateOf(setOf<Long>()) }
    val history = rememberSaveable { mutableStateOf(setOf<Long>()) }

    // ── Medico Scelto ────────────────────────────────────────────────────────
    var doctorList by remember { mutableStateOf<List<Medico>>(emptyList()) }
    var selectedDoctor by remember { mutableStateOf<Medico?>(null) }
    var doctorExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        doctorList = database.medicoDao().getTuttiIMedici()
    }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var showErrors by remember { mutableStateOf(false) }

    val isFormValid = ValidationUtils.isValidEmail(email) && 
            ValidationUtils.isValidPassword(password) && 
            password == confirmPassword &&
            name.isNotBlank() && 
            surname.isNotBlank() &&
            sex != null && 
            biologic != null &&
            selectedDoctor != null &&
            DateUtils.isValidDate(birthDate)

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
                colors = outlinedFieldColors(),
                isError = showErrors && !ValidationUtils.isValidEmail(email)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = outlinedFieldColors(),
                isError = (password.isNotEmpty() && !ValidationUtils.isValidPassword(password)) || (showErrors && password.isEmpty()),
                supportingText = {
                    if (password.isNotEmpty() && !ValidationUtils.isValidPassword(password)) {
                        Text("Min. 10 car., 1 Maiusc., 1 Minusc., 1 Num., 1 Spec.")
                    }
                }
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Conferma Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = outlinedFieldColors(),
                isError = showErrors && (confirmPassword != password || confirmPassword.isEmpty())
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Slate200)

            // ── Medico Curante ───────────────────────────────────────────────
            SectionLabel("Medico Curante")
            ExposedDropdownMenuBox(
                expanded = doctorExpanded,
                onExpandedChange = { doctorExpanded = !doctorExpanded }
            ) {
                OutlinedTextField(
                    value = selectedDoctor?.let { "${it.nome} ${it.cognome}" } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text("Seleziona medico…", color = Slate400) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = doctorExpanded) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = outlinedFieldColors(),
                    isError = showErrors && selectedDoctor == null
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
                birthDate = birthDate,
                onBirthDateChange = { birthDate = it },
                sex = sex,
                onSexChange = { sex = it },
                biologic = biologic,
                onBiologicChange = { biologic = it },
                conditions = conditions.value,
                onConditionsChange = { conditions.value = it },
                history = history.value,
                onHistoryChange = { history.value = it },
                showErrors = showErrors,
                biologicOptions = biologicOptions,
                conditionOptions = conditionOptions,
                vaccineOptions = vaccineOptions
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
                            val idCuraSelezionata = biologic?.idCura ?: 1L
                            
                            // 3. Salvataggio Paziente
                            val idPaziente = database.pazienteDao().inserisciPaziente(
                                Paziente(
                                    idUtente = idUtente,
                                    idMedico = selectedDoctor?.idMedico ?: 1L, 
                                    idCura = idCuraSelezionata,
                                    nome = name,
                                    cognome = surname,
                                    dataNascita = birthDate,
                                    sesso = if (sex == "Maschio") Sesso.MASCHIO else Sesso.FEMMINA
                                )
                            )

                            // 4. Salvataggio Condizioni Cliniche
                            conditions.value.forEach { condId ->
                                database.pazienteCondizioneDao().inserisciPazienteCondizione(
                                    PazienteCondizione(idPaziente = idPaziente, idCondizione = condId)
                                )
                            }

                            // 5. Salvataggio Storia Vaccinale
                            history.value.forEach { vaccinoId ->
                                database.vaccinazioneDao().inserisciVaccinazione(
                                    Vaccinazione(
                                        idPaziente = idPaziente,
                                        idVaccino = vaccinoId
                                    )
                                )
                            }
                            
                            onRegisterSuccess(email)
                        }
                    } else {
                        showErrors = true
                    }
                },
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
