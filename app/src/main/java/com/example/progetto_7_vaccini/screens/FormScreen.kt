package com.example.progetto_7_vaccini.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.progetto_7_vaccini.data.BiologicType
import com.example.progetto_7_vaccini.data.MedicalCondition
import com.example.progetto_7_vaccini.data.Sex
import com.example.progetto_7_vaccini.data.DateUtils
import com.example.progetto_7_vaccini.data.ValidationUtils
import com.example.progetto_7_vaccini.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    initialName: String = "",
    initialSurname: String = "",
    initialBirthDate: String = "",
    initialSex: Sex? = null,
    initialBiologic: BiologicType? = null,
    initialConditions: Set<MedicalCondition> = emptySet(),
    initialHistory: Set<String> = emptySet(),
    onBack: () -> Unit,
    onSubmit: (nome: String, cognome: String, birthDate: String, sex: Sex, biologic: BiologicType, conditions: Set<MedicalCondition>, history: Set<String>) -> Unit
) {
    var name         by rememberSaveable { mutableStateOf(initialName) }
    var surname      by rememberSaveable { mutableStateOf(initialSurname) }
    var birthDate    by rememberSaveable { mutableStateOf(initialBirthDate) }
    var sex          by rememberSaveable { mutableStateOf<Sex?>(initialSex) }
    var biologic     by rememberSaveable { mutableStateOf<BiologicType?>(initialBiologic) }
    val conditions   = rememberSaveable { mutableStateOf(initialConditions) }
    val history      = rememberSaveable { mutableStateOf(initialHistory) }

    val isValid = name.isNotBlank() && surname.isNotBlank() && sex != null && biologic != null

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
                        text = "STRUMENTO CLINICO",
                        style = MaterialTheme.typography.labelSmall,
                        color = Teal100,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "Guida alla vaccinazione in\nterapia biologica",
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Inserisci i dati del paziente per ottenere raccomandazioni vaccinali personalizzate in base al tipo di terapia biologica e alle condizioni cliniche associate.",
                style = MaterialTheme.typography.bodySmall,
                color = Slate600
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
                onHistoryChange = { history.value = it }
            )

            // ── Submit ────────────────────────────────────────────────────────
            Spacer(Modifier.height(4.dp))
            Button(
                onClick  = { 
                    if (isValid) {
                        onSubmit(
                            name,
                            surname,
                            birthDate, 
                            sex!!, 
                            biologic!!, 
                            conditions.value, 
                            history.value
                        ) 
                    }
                },
                enabled  = isValid && DateUtils.isValidDate(birthDate),
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
                    text       = "Genera raccomandazioni",
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 15.sp
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun VaccineFormContent(
    name: String,
    onNameChange: (String) -> Unit,
    surname: String,
    onSurnameChange: (String) -> Unit,
    birthDate: String,
    onBirthDateChange: (String) -> Unit,
    sex: Sex?,
    onSexChange: (Sex) -> Unit,
    biologic: BiologicType?,
    onBiologicChange: (BiologicType) -> Unit,
    conditions: Set<MedicalCondition>,
    onConditionsChange: (Set<MedicalCondition>) -> Unit,
    history: Set<String>,
    onHistoryChange: (Set<String>) -> Unit
) {
    var sexExpanded      by remember { mutableStateOf(false) }
    var biologicExpanded by remember { mutableStateOf(false) }

    val vaccineHistoryOptions = listOf(
        "Influenza (vaccino inattivato o ricombinante)",
        "Pneumococcico coniugato (PCV15 o PCV20)",
        "Epatite B",
        "Herpes Zoster ricombinante (Shingrix)",
        "Td / Tdap (Tetano-Difterite-Pertosse)",
        "COVID-19 (mRNA o subunità proteica)"
    )

    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        // ── Patient name & surname ────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                SectionLabel("Nome")
                OutlinedTextField(
                    value         = name,
                    onValueChange = { if (ValidationUtils.isValidName(it)) onNameChange(it) },
                    placeholder   = { Text("Es. Mario", color = Slate400) },
                    modifier      = Modifier.fillMaxWidth(),
                    singleLine    = true,
                    shape         = RoundedCornerShape(12.dp),
                    colors        = outlinedFieldColors()
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                SectionLabel("Cognome")
                OutlinedTextField(
                    value         = surname,
                    onValueChange = { if (ValidationUtils.isValidName(it)) onSurnameChange(it) },
                    placeholder   = { Text("Es. Rossi", color = Slate400) },
                    modifier      = Modifier.fillMaxWidth(),
                    singleLine    = true,
                    shape         = RoundedCornerShape(12.dp),
                    colors        = outlinedFieldColors()
                )
            }
        }

        // ── Birth Date ───────────────────────────────────────────────────
        SectionLabel("Data di nascita")
        
        var showDatePicker by remember { mutableStateOf(false) }
        val datePickerState = rememberDatePickerState()

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onBirthDateChange(DateUtils.formatTimestamp(it))
                        }
                        showDatePicker = false
                    }) {
                        Text("OK", color = Teal700, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("ANNULLA", color = Slate600)
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        OutlinedTextField(
            value         = birthDate,
            onValueChange = { },
            readOnly      = true,
            placeholder   = { Text("Seleziona data…", color = Slate400) },
            trailingIcon  = { 
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Seleziona data",
                        tint = Teal700
                    )
                }
            },
            modifier      = Modifier.fillMaxWidth().clickable { showDatePicker = true },
            shape         = RoundedCornerShape(12.dp),
            colors        = outlinedFieldColors(),
            isError       = birthDate.isNotEmpty() && !DateUtils.isValidDate(birthDate)
        )

        // ── Sex dropdown ──────────────────────────────────────────────────
        SectionLabel("Sesso biologico")
        ExposedDropdownMenuBox(
            expanded        = sexExpanded,
            onExpandedChange = { sexExpanded = !sexExpanded }
        ) {
            OutlinedTextField(
                value         = sex?.label ?: "",
                onValueChange = {},
                readOnly      = true,
                placeholder   = { Text("Seleziona…", color = Slate400) },
                trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sexExpanded) },
                modifier      = Modifier.fillMaxWidth().menuAnchor(),
                shape         = RoundedCornerShape(12.dp),
                colors        = outlinedFieldColors()
            )
            ExposedDropdownMenu(
                expanded        = sexExpanded,
                onDismissRequest = { sexExpanded = false }
            ) {
                Sex.entries.forEach { option ->
                    DropdownMenuItem(
                        text    = { Text(option.label) },
                        onClick = {
                            onSexChange(option)
                            sexExpanded = false
                        }
                    )
                }
            }
        }

        // ── Biologic dropdown ─────────────────────────────────────────────
        SectionLabel("Tipo di terapia biologica")
        ExposedDropdownMenuBox(
            expanded        = biologicExpanded,
            onExpandedChange = { biologicExpanded = !biologicExpanded }
        ) {
            OutlinedTextField(
                value         = biologic?.label ?: "",
                onValueChange = {},
                readOnly      = true,
                placeholder   = { Text("Seleziona tipo…", color = Slate400) },
                trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = biologicExpanded) },
                modifier      = Modifier.fillMaxWidth().menuAnchor(),
                shape         = RoundedCornerShape(12.dp),
                colors        = outlinedFieldColors()
            )
            ExposedDropdownMenu(
                expanded        = biologicExpanded,
                onDismissRequest = { biologicExpanded = false },
                modifier        = Modifier.heightIn(max = 320.dp)
            ) {
                BiologicType.entries.forEach { option ->
                    DropdownMenuItem(
                        text    = { Text(option.label, style = MaterialTheme.typography.bodySmall) },
                        onClick = {
                            onBiologicChange(option)
                            biologicExpanded = false
                        }
                    )
                }
            }
        }

        // ── Medical conditions ────────────────────────────────────────────
        SectionLabel("Condizioni mediche rilevanti  •  seleziona tutte le pertinenti")
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            MedicalCondition.entries.forEach { condition ->
                val checked = conditions.contains(condition)
                ConditionCheckItem(
                    label   = condition.label,
                    checked = checked,
                    onClick = {
                        onConditionsChange(
                            if (checked) conditions - condition else conditions + condition
                        )
                    }
                )
            }
        }

        // ── Vaccine history ───────────────────────────────────────────────
        SectionLabel("Storia vaccinale  •  seleziona i vaccini già completati")
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            vaccineHistoryOptions.forEach { vaccineName ->
                val isSelected = history.contains(vaccineName)
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        onHistoryChange(
                            if (isSelected) history - vaccineName else history + vaccineName
                        )
                    },
                    label = { Text(vaccineName, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Teal100,
                        selectedLabelColor = Teal900,
                        containerColor = Color.White
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = Slate200,
                        selectedBorderColor = Teal600
                    )
                )
            }
        }
    }
}

// ── Shared helpers ────────────────────────────────────────────────────────────

@Composable
fun SectionLabel(text: String) {
    Text(
        text     = text.uppercase(),
        style    = MaterialTheme.typography.labelSmall,
        color    = Slate600,
        modifier = Modifier.padding(bottom = 2.dp)
    )
}

@Composable
fun ConditionCheckItem(label: String, checked: Boolean, onClick: () -> Unit) {
    val bgColor     = if (checked) Teal50     else Color.White
    val borderColor = if (checked) Teal600    else Slate200
    val textColor   = if (checked) Teal900    else Slate800

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(if (checked) Teal600 else Color.White)
                .border(2.dp, if (checked) Teal600 else Slate400, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Icon(
                    imageVector        = Icons.Default.Check,
                    contentDescription = null,
                    tint               = Color.White,
                    modifier           = Modifier.size(12.dp)
                )
            }
        }
        Text(
            text  = label,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor   = Teal700,
    unfocusedBorderColor = Slate200,
    focusedLabelColor    = Teal700,
    cursorColor          = Teal700
)
