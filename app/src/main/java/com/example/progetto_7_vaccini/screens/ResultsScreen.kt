package com.example.progetto_7_vaccini.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.progetto_7_vaccini.data.database.*
import com.example.progetto_7_vaccini.data.database.entities.*
import com.example.progetto_7_vaccini.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    patientName    : String,
    patientSurname : String,
    patientAge     : Int?,
    sex            : String,
    biologic       : CuraBiologica,
    recommendations: List<VaccineRec>,
    onBack         : () -> Unit
) {
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.background(Teal900)
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.statusBars)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector        = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Indietro",
                            tint               = Color.White
                        )
                    }
                    Text(
                        text  = "Raccomandazioni vaccinali",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { padding ->
        ResultsContent(
            patientName = patientName,
            patientSurname = patientSurname,
            patientAge = patientAge,
            sex = sex,
            biologic = biologic,
            recommendations = recommendations,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun ResultsContent(
    patientName: String,
    patientSurname: String,
    patientAge: Int?,
    sex: String,
    biologic: CuraBiologica,
    recommendations: List<VaccineRec>,
    modifier: Modifier = Modifier
) {
    val recommended    = recommendations.filter { it.status == VaccineStatus.RECOMMENDED }
    val contraindicated = recommendations.filter { it.status == VaccineStatus.CONTRAINDICATED }
    val alreadyDone    = recommendations.filter { it.status == VaccineStatus.ALREADY_DONE }
    
    val essential      = recommended.filter { it.priority == VaccinePriority.ESSENTIAL }
    val routine        = recommended.filter { it.priority != VaccinePriority.ESSENTIAL }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Patient summary card
        item {
            PatientSummaryCard(
                name     = patientName,
                surname  = patientSurname,
                age      = patientAge,
                sex      = sex,
                biologic = biologic
            )
        }

        // Disclaimer
        item { DisclaimerCard() }

        // ── Recommended ────────────────────────────────────────────────────
        item {
            SectionHeader(
                title     = "Vaccini raccomandati",
                count     = recommended.size,
                isPositive = true
            )
        }

        if (essential.isNotEmpty()) {
            item {
                Text(
                    text     = "PRIORITARI / ESSENZIALI",
                    style    = MaterialTheme.typography.labelSmall,
                    color    = Amber700,
                    modifier = Modifier.padding(start = 2.dp, bottom = 4.dp)
                )
            }
            items(essential) { vaccine ->
                VaccineCard(vaccine = vaccine)
            }
        }

        if (routine.isNotEmpty()) {
            item {
                Text(
                    text     = "DI ROUTINE / CONDIZIONALI",
                    style    = MaterialTheme.typography.labelSmall,
                    color    = Slate400,
                    modifier = Modifier.padding(start = 2.dp, top = 8.dp, bottom = 4.dp)
                )
            }
            items(routine) { vaccine ->
                VaccineCard(vaccine = vaccine)
            }
        }

        // ── Contraindicated ────────────────────────────────────────────────
        item {
            Spacer(Modifier.height(4.dp))
            SectionHeader(
                title     = "Vaccini controindicati",
                count     = contraindicated.size,
                isPositive = false
            )
        }

        items(contraindicated) { vaccine ->
            VaccineCard(vaccine = vaccine)
        }

        // ── Already Done ──────────────────────────────────────────────────
        if (alreadyDone.isNotEmpty()) {
            item {
                Spacer(Modifier.height(4.dp))
                SectionHeader(
                    title     = "Vaccini completati",
                    count     = alreadyDone.size,
                    isPositive = true // Usiamo verde per indicare completato
                    )
            }
            items(alreadyDone) { vaccine ->
                VaccineCard(vaccine = vaccine)
            }
        }

        // Footer
        item {
            Spacer(Modifier.height(8.dp))
            Text(
                text     = "Basato sulle linee guida ACIP, EULAR, ECDC e SEPAR per la vaccinazione nei pazienti immunocompromessi.",
                style    = MaterialTheme.typography.bodySmall,
                color    = Slate400,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Spacer(Modifier.height(32.dp))
        }
    }
}

// ── Components ────────────────────────────────────────────────────────────────

@Composable
private fun PatientSummaryCard(name: String, surname: String, age: Int?, sex: String, biologic: CuraBiologica) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LabeledValue(label = "Paziente", value = "$name $surname")
                LabeledValue(label = "Terapia biologica", value = biologic.nome)
            }
            Column(modifier = Modifier.width(IntrinsicSize.Max), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                LabeledValue(label = "Età", value = age?.toString() ?: "N/D")
                LabeledValue(label = "Sesso", value = sex)
            }
        }
    }
}

@Composable
private fun LabeledValue(label: String, value: String) {
    Column {
        Text(
            text  = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = Slate400
        )
        Text(
            text       = value,
            style      = MaterialTheme.typography.bodyMedium,
            color      = Slate800,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DisclaimerCard() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Amber50)
            .border(1.dp, Amber100, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector        = Icons.Default.Warning,
            contentDescription = null,
            tint               = Amber700,
            modifier           = Modifier.size(18.dp).padding(top = 2.dp)
        )
        Text(
            text  = "Strumento di supporto alle decisioni cliniche. Queste raccomandazioni si basano sulle linee guida di immunizzazione vigenti (ACIP, EULAR, ECDC). Le decisioni individuali devono essere prese insieme al medico responsabile, considerando l'intero contesto clinico.",
            style = MaterialTheme.typography.bodySmall,
            color = Amber700,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun SectionHeader(title: String, count: Int, isPositive: Boolean) {
    val iconBg    = if (isPositive) Emerald100 else Red100
    val iconTint  = if (isPositive) Emerald700  else Red700

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = if (isPositive) Icons.Default.Check else Icons.Default.Close,
                contentDescription = null,
                tint               = iconTint,
                modifier           = Modifier.size(18.dp)
            )
        }
        Column {
            Text(
                text       = title,
                style      = MaterialTheme.typography.headlineMedium,
                color      = Slate800,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text  = "$count vaccini · tocca una scheda per i dettagli",
                style = MaterialTheme.typography.bodySmall,
                color = Slate400
            )
        }
    }
}

@Composable
private fun VaccineCard(vaccine: VaccineRec) {
    var expanded by remember { mutableStateOf(false) }

    val isContra    = vaccine.status == VaccineStatus.CONTRAINDICATED
    val isDone      = vaccine.status == VaccineStatus.ALREADY_DONE
    
    val borderColor = when {
        isContra -> Color(0xFFFCA5A5)
        isDone   -> Slate200
        else     -> Color(0xFF6EE7B7)
    }
    
    val bgColor     = when {
        isContra -> Color(0xFFFFF5F5)
        isDone   -> Slate50
        else     -> Color.White
    }

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        shape     = RoundedCornerShape(14.dp),
        colors    = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (expanded) 3.dp else 1.dp),
        border    = BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    // Name + type pill
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text       = vaccine.name,
                            style      = MaterialTheme.typography.titleMedium,
                            color      = Slate800,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TypePill(type = vaccine.type)
                        StatusBadge(status = vaccine.status)
                        if (vaccine.priority == VaccinePriority.ESSENTIAL) {
                            PriorityBadge()
                        }
                    }
                    vaccine.brand?.let { brand ->
                        Text(
                            text     = brand,
                            style    = MaterialTheme.typography.bodySmall,
                            color    = Slate400,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
                Icon(
                    imageVector        = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Comprimi" else "Espandi",
                    tint               = Slate400,
                    modifier           = Modifier.size(20.dp)
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter   = expandVertically(),
                exit    = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                ) {
                    HorizontalDivider(color = Slate200, thickness = 1.dp)
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text  = vaccine.reason,
                        style = MaterialTheme.typography.bodySmall,
                        color = Slate600,
                        lineHeight = 18.sp
                    )
                    vaccine.timing?.let { timing ->
                        Spacer(Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector        = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint               = Teal700,
                                modifier           = Modifier.size(14.dp).padding(top = 2.dp)
                            )
                            Text(
                                text  = timing,
                                style = MaterialTheme.typography.bodySmall,
                                color = Teal900,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TypePill(type: VaccineType) {
    val (bg, fg) = when (type) {
        VaccineType.LIVE         -> Color(0xFFFFF7ED) to Color(0xFFC2410C)
        VaccineType.INACTIVATED  -> Color(0xFFF1F5F9) to Color(0xFF475569)
        VaccineType.RECOMBINANT  -> Color(0xFFEEF2FF) to Color(0xFF4338CA)
        VaccineType.SUBUNIT      -> Color(0xFFEFF6FF) to Color(0xFF1D4ED8)
        VaccineType.MRNA         -> Color(0xFFF5F3FF) to Color(0xFF6D28D9)
        VaccineType.TOXOID       -> Color(0xFFF0FDFA) to Color(0xFF0F766E)
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(bg)
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text       = type.name,
            color      = fg,
            fontSize   = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.6.sp
        )
    }
}

@Composable
private fun StatusBadge(status: VaccineStatus) {
    val (bg, fg, dot) = when (status) {
        VaccineStatus.RECOMMENDED    -> Triple(Emerald100, Emerald700, Emerald700)
        VaccineStatus.CONTRAINDICATED -> Triple(Red100,     Red700,     Red700)
        VaccineStatus.CAUTION        -> Triple(Color(0xFFFEF3C7), Amber700, Amber700)
        VaccineStatus.ALREADY_DONE    -> Triple(Slate100, Slate600, Slate400)
    }
    val label = when (status) {
        VaccineStatus.RECOMMENDED    -> "Raccomandato"
        VaccineStatus.CONTRAINDICATED -> "Controindicato"
        VaccineStatus.CAUTION        -> "Precauzione"
        VaccineStatus.ALREADY_DONE    -> "Già completato"
    }
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(dot)
        )
        Text(text = label, color = fg, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun PriorityBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Amber50)
            .border(1.dp, Amber100, RoundedCornerShape(4.dp))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text       = "PRIORITARIO",
            color      = Amber700,
            fontSize   = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.6.sp
        )
    }
}