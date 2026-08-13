package com.example.progetto_7_vaccini.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.example.progetto_7_vaccini.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScreen(
    onSubmit: (name: String, age: Int?, sex: Sex, biologic: BiologicType, conditions: Set<MedicalCondition>, history: Set<String>) -> Unit
) {
    var name         by rememberSaveable { mutableStateOf("") }
    var ageStr       by rememberSaveable { mutableStateOf("") }
    var sex          by rememberSaveable { mutableStateOf<Sex?>(null) }
    var biologic     by rememberSaveable { mutableStateOf<BiologicType?>(null) }
    val conditions   = rememberSaveable { mutableStateOf(setOf<MedicalCondition>()) }
    val history      = rememberSaveable { mutableStateOf(setOf<String>()) }

    var sexExpanded      by remember { mutableStateOf(false) }
    var biologicExpanded by remember { mutableStateOf(false) }

    val isValid = name.isNotBlank() && sex != null && biologic != null

    val vaccineHistoryOptions = listOf(
        "Influenza (vaccino inattivato o ricombinante)",
        "Pneumococcico coniugato (PCV15 o PCV20)",
        "Epatite B",
        "Herpes Zoster ricombinante (Shingrix)",
        "Td / Tdap (Tetano-Difterite-Pertosse)",
        "COVID-19 (mRNA o subunità proteica)"
    )

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(Teal900)
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
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

            // ── Patient name ──────────────────────────────────────────────────
            SectionLabel("Nome del paziente")
            OutlinedTextField(
                value         = name,
                onValueChange = { name = it },
                placeholder   = { Text("Es. Mario Rossi", color = Slate400) },
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true,
                shape         = RoundedCornerShape(12.dp),
                colors        = outlinedFieldColors()
            )

            // ── Age ──────────────────────────────────────────────────────────
            SectionLabel("Età")
            OutlinedTextField(
                value         = ageStr,
                onValueChange = { if (it.length <= 3 && it.all { c -> c.isDigit() }) ageStr = it },
                placeholder   = { Text("Es. 45", color = Slate400) },
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true,
                shape         = RoundedCornerShape(12.dp),
                colors        = outlinedFieldColors()
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
                                sex = option
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
                                biologic = option
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
                    val checked = conditions.value.contains(condition)
                    ConditionCheckItem(
                        label   = condition.label,
                        checked = checked,
                        onClick = {
                            conditions.value = if (checked) {
                                conditions.value - condition
                            } else {
                                conditions.value + condition
                            }
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
                    val isSelected = history.value.contains(vaccineName)
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            history.value = if (isSelected) {
                                history.value - vaccineName
                            } else {
                                history.value + vaccineName
                            }
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

            // ── Submit ────────────────────────────────────────────────────────
            Spacer(Modifier.height(4.dp))
            Button(
                onClick  = { 
                    if (isValid) {
                        onSubmit(
                            name, 
                            ageStr.toIntOrNull(), 
                            sex!!, 
                            biologic!!, 
                            conditions.value, 
                            history.value
                        ) 
                    }
                },
                enabled  = isValid,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(
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

// ── Shared helpers ────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Text(
        text     = text.uppercase(),
        style    = MaterialTheme.typography.labelSmall,
        color    = Slate600,
        modifier = Modifier.padding(bottom = 2.dp)
    )
}

@Composable
private fun ConditionCheckItem(label: String, checked: Boolean, onClick: () -> Unit) {
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
private fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor   = Teal700,
    unfocusedBorderColor = Slate200,
    focusedLabelColor    = Teal700,
    cursorColor          = Teal700
)