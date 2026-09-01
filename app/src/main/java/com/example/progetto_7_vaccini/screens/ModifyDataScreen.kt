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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.progetto_7_vaccini.data.DateUtils
import com.example.progetto_7_vaccini.data.ValidationUtils
import com.example.progetto_7_vaccini.data.database.entities.CondizioneClinica
import com.example.progetto_7_vaccini.data.database.entities.CuraBiologica
import com.example.progetto_7_vaccini.data.database.entities.Sesso
import com.example.progetto_7_vaccini.data.database.entities.Vaccino
import com.example.progetto_7_vaccini.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModifyDataScreen(
    initialName: String,
    initialSurname: String,
    initialBirthDate: String,
    initialSex: Sesso?,
    initialBiologic: CuraBiologica?,
    initialConditions: Set<Long>,
    initialHistory: Set<Long>,
    initialEmail: String,
    biologicOptions: List<CuraBiologica> = emptyList(),
    conditionOptions: List<CondizioneClinica> = emptyList(),
    vaccineOptions: List<Vaccino> = emptyList(),
    onBack: () -> Unit,
    onEmailUpdate: (String, (String?) -> Unit) -> Unit,
    onConfirm: (nome: String, cognome: String, birthDate: String, sex: Sesso, biologic: CuraBiologica, conditions: Set<Long>, history: Set<Long>) -> Unit
) {
    var name         by rememberSaveable { mutableStateOf(initialName) }
    var surname      by rememberSaveable { mutableStateOf(initialSurname) }
    var birthDate    by rememberSaveable { mutableStateOf(initialBirthDate) }
    var sex          by rememberSaveable { mutableStateOf<Sesso?>(initialSex) }
    var biologic     by rememberSaveable { mutableStateOf<CuraBiologica?>(initialBiologic) }
    val conditions   = rememberSaveable { mutableStateOf(initialConditions) }
    val history      = rememberSaveable { mutableStateOf(initialHistory) }

    var showErrors by remember { mutableStateOf(false) }

    var email        by rememberSaveable { mutableStateOf(initialEmail) }
    var isEmailEditable by remember { mutableStateOf(false) }
    var emailError   by rememberSaveable { mutableStateOf<String?>(null) }

    val isFormValid = name.isNotBlank() && 
                     surname.isNotBlank() && 
                     sex != null && 
                     biologic != null && 
                     DateUtils.isValidDate(birthDate)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MODIFICA DATI", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Indietro")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Teal900,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Sezione Email
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SectionLabel("Email dell'account")
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { 
                            email = it
                            emailError = null
                        },
                        modifier = Modifier.weight(1f),
                        readOnly = !isEmailEditable,
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = outlinedFieldColors(),
                        isError = emailError != null,
                        textStyle = LocalTextStyle.current.copy(
                            color = if (isEmailEditable) Color.Black else Color.Gray
                        )
                    )
                    Button(
                        onClick = {
                            if (isEmailEditable) {
                                if (ValidationUtils.isValidEmail(email)) {
                                    onEmailUpdate(email) { error ->
                                        if (error == null) {
                                            isEmailEditable = false
                                            emailError = null
                                        } else {
                                            emailError = error
                                        }
                                    }
                                } else {
                                    emailError = "Email non valida"
                                }
                            } else {
                                isEmailEditable = true
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isEmailEditable) Emerald700 else Teal700
                        )
                    ) {
                        Text(if (isEmailEditable) "CONFERMA" else "MODIFICA")
                    }
                }
                if (emailError != null) {
                    Text(
                        text = emailError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            HorizontalDivider(color = Slate200, thickness = 1.dp)

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

            // ── Conferma Dati ──────────────────────────────────────────────────
            Spacer(Modifier.height(4.dp))
            Button(
                onClick  = { 
                    if (isFormValid) {
                        onConfirm(
                            name,
                            surname,
                            birthDate, 
                            sex!!, 
                            biologic!!, 
                            conditions.value, 
                            history.value
                        ) 
                    } else {
                        showErrors = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor         = Teal900,
                    contentColor           = Color.White,
                    disabledContainerColor = Slate200,
                    disabledContentColor   = Slate400
                )
            ) {
                Text(
                    text       = "CONFERMA MODIFICHE",
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 15.sp
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
