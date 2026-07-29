package com.example.progetto_7_vaccini.data

// ── Enumerazioni ──────────────────────────────────────────────────────────────

enum class Sex(val label: String) {
    FEMALE("Femmina"),
    MALE("Maschio"),
    OTHER("Altro / non binario")
}

enum class BiologicType(val label: String, val shortName: String) {
    TNF_INHIBITOR(
        "Inibitore TNF-α (adalimumab, etanercept, infliximab, certolizumab, golimumab)",
        "Anti-TNF"
    ),
    IL6_INHIBITOR(
        "Inibitore IL-6 (tocilizumab, sarilumab)",
        "Anti-IL-6"
    ),
    IL17_INHIBITOR(
        "Inibitore IL-17 (secukinumab, ixekizumab)",
        "Anti-IL-17"
    ),
    IL12_23_INHIBITOR(
        "Inibitore IL-12/23 (ustekinumab)",
        "Anti-IL-12/23"
    ),
    IL23_INHIBITOR(
        "Inibitore IL-23 (guselkumab, risankizumab)",
        "Anti-IL-23"
    ),
    B_CELL_DEPLETER(
        "Deplettore di cellule B (rituximab, obinutuzumab)",
        "Anti-CD20"
    ),
    T_CELL_COSTIM(
        "Bloccante della co-stimolazione T (abatacept)",
        "Anti-CTLA-4"
    ),
    JAK_INHIBITOR(
        "Inibitore JAK (tofacitinib, baricitinib, upadacitinib)",
        "JAKi"
    ),
    VEGF_INHIBITOR(
        "Inibitore VEGF (bevacizumab, ramucirumab)",
        "Anti-VEGF"
    ),
    BLYS_INHIBITOR(
        "Inibitore BLyS/BAFF (belimumab)",
        "Anti-BLyS"
    ),
    GUT_SELECTIVE(
        "Inibitore dell'integrina selettivo intestinale (vedolizumab, natalizumab)",
        "Anti-integrina"
    )
}

enum class MedicalCondition(val label: String) {
    ASPLENIA("Asplenia / asplenia funzionale"),
    CHRONIC_KIDNEY_DISEASE("Malattia renale cronica / insufficienza renale"),
    DIABETES("Diabete mellito"),
    COPD("BPCO / malattia polmonare cronica"),
    LIVER_DISEASE("Epatopatia cronica / cirrosi"),
    HIV("Infezione da HIV"),
    HEART_DISEASE("Cardiopatia cronica / insufficienza cardiaca"),
    PREGNANCY("Gravidanza"),
    CANCER("Neoplasia solida / ematologica"),
    IBD("Malattia infiammatoria intestinale (Crohn / RCU)")
}

// ── Modelli di dominio ────────────────────────────────────────────────────────

enum class VaccineStatus { RECOMMENDED, CONTRAINDICATED, CAUTION }
enum class VaccinePriority { ESSENTIAL, HIGH, ROUTINE }
enum class VaccineType { LIVE, INACTIVATED, RECOMBINANT, SUBUNIT, MRNA, TOXOID }
enum class ImmunoLevel(val label: String, val color: ImmunoColor) {
    LOW("Bassa", ImmunoColor.GREEN),
    MODERATE("Moderata", ImmunoColor.YELLOW),
    HIGH("Alta", ImmunoColor.ORANGE),
    SEVERE("Molto alta", ImmunoColor.RED)
}
enum class ImmunoColor { GREEN, YELLOW, ORANGE, RED }

data class VaccineRec(
    val name: String,
    val brand: String? = null,
    val type: VaccineType,
    val status: VaccineStatus,
    val reason: String,
    val timing: String? = null,
    val priority: VaccinePriority = VaccinePriority.ROUTINE
)

/** Contesto clinico per biológico, mostrato in evidenza nella schermata risultati. */
data class BiologicProfile(
    val immunoLevel: ImmunoLevel,
    val keyRisks: List<String>,
    val generalNote: String,
    val preStartNote: String? = null
)

// ── Profili per biologico ─────────────────────────────────────────────────────

val biologicProfiles: Map<BiologicType, BiologicProfile> = mapOf(

    BiologicType.TNF_INHIBITOR to BiologicProfile(
        immunoLevel  = ImmunoLevel.HIGH,
        keyRisks     = listOf(
            "Riattivazione dell'HBV (fino al 39% dei portatori senza profilassi)",
            "Riattivazione della tubercolosi latente",
            "Rischio aumentato di herpes zoster",
            "Maggiore suscettibilità alle infezioni opportunistiche"
        ),
        generalNote  = "Gli anti-TNF bloccano una citochina chiave dell'immunità innata e adattativa, aumentando il rischio di infezioni batteriche, micobatteriche e virali. La vaccinazione prima dell'inizio del trattamento massimizza la risposta immune.",
        preStartNote = "Eseguire screening per HBV (HBsAg, anti-HBc, anti-HBs) e tubercolosi (IGRA o Mantoux) obbligatoriamente prima di iniziare. Vaccinare almeno 2–4 settimane prima dell'inizio ove possibile."
    ),

    BiologicType.IL6_INHIBITOR to BiologicProfile(
        immunoLevel  = ImmunoLevel.HIGH,
        keyRisks     = listOf(
            "Mascheramento di febbre e infiammazione (rende difficile la diagnosi di infezione)",
            "Rischio aumentato di infezioni respiratorie gravi",
            "Rischio di riattivazione dell'HBV",
            "Neutropenia funzionale"
        ),
        generalNote  = "Gli inibitori di IL-6 bloccano la risposta di fase acuta: il paziente può avere un'infezione grave senza febbre né aumento della PCR. L'influenza è particolarmente pericolosa perché può progredire silenziosamente.",
        preStartNote = "Screening per HBV e tubercolosi prima di iniziare. La vaccinazione antinfluenzale è particolarmente prioritaria dato il mascheramento dei sintomi."
    ),

    BiologicType.IL17_INHIBITOR to BiologicProfile(
        immunoLevel  = ImmunoLevel.MODERATE,
        keyRisks     = listOf(
            "Candidosi mucocutanea (IL-17 protegge contro i funghi)",
            "Infezioni ricorrenti delle vie respiratorie superiori",
            "Potenziale riattivazione della tubercolosi (rischio minore rispetto agli anti-TNF)"
        ),
        generalNote  = "Gli anti-IL-17 hanno un profilo di immunosoppressione più selettivo rispetto agli anti-TNF. Il rischio di infezioni gravi è inferiore, ma la vaccinazione rimane importante nel contesto della malattia infiammatoria cronica.",
        preStartNote = "Screening per tubercolosi raccomandato prima di iniziare. Vaccinazione anti-pneumococcica e antinfluenzale prioritarie."
    ),

    BiologicType.IL12_23_INHIBITOR to BiologicProfile(
        immunoLevel  = ImmunoLevel.MODERATE,
        keyRisks     = listOf(
            "Maggiore suscettibilità alle infezioni micobatteriche (IL-12 è fondamentale per il controllo dei micobatteri)",
            "Rischio aumentato di infezioni fungine",
            "Infezioni respiratorie"
        ),
        generalNote  = "L'IL-12 è essenziale per l'immunità contro micobatteri e funghi. Ustekinumab ha un profilo di sicurezza complessivamente favorevole, ma lo screening per la tubercolosi e la vaccinazione completa sono essenziali.",
        preStartNote = "Screening per tubercolosi obbligatorio prima di iniziare. Vaccinare almeno 2 settimane prima della prima dose."
    ),

    BiologicType.IL23_INHIBITOR to BiologicProfile(
        immunoLevel  = ImmunoLevel.LOW,
        keyRisks     = listOf(
            "Infezioni delle vie respiratorie superiori (le più frequenti)",
            "Candidosi (meno comune rispetto agli anti-IL-17)",
            "Rischio globale di infezione grave basso"
        ),
        generalNote  = "Gli inibitori selettivi di IL-23 hanno il profilo di immunosoppressione più favorevole tra i biologici. Il rischio di infezioni gravi è basso, ma la vaccinazione completa è raccomandata per la patologia di base.",
        preStartNote = "Screening per tubercolosi raccomandato. Vaccinazione di routine prima di iniziare, preferibilmente."
    ),

    BiologicType.B_CELL_DEPLETER to BiologicProfile(
        immunoLevel  = ImmunoLevel.SEVERE,
        keyRisks     = listOf(
            "Deplezione profonda delle cellule B → risposta umorale quasi nulla durante il trattamento",
            "Ipogammaglobulinemia progressiva con dosi ripetute",
            "Alto rischio di infezioni da germi capsulati (pneumococco, meningococco, Hib)",
            "Riattivazione dell'HBV (potenzialmente fulminante)",
            "Riattivazione del virus JC → leucoencefalopatia multifocale progressiva"
        ),
        generalNote  = "ATTENZIONE: rituximab e altri deplettori delle cellule B eliminano la capacità di generare anticorpi de novo per mesi. I vaccini somministrati durante il trattamento attivo hanno efficacia minima o nulla. La finestra di vaccinazione è critica.",
        preStartNote = "URGENTE: vaccinare con almeno 4 settimane di anticipo rispetto all'inizio. Se il trattamento è già iniziato, attendere ≥6 mesi dopo l'ultima infusione prima di vaccinare. Verificare sierologie HBV (rischio di riattivazione fulminante). Monitorare i livelli di immunoglobuline."
    ),

    BiologicType.T_CELL_COSTIM to BiologicProfile(
        immunoLevel  = ImmunoLevel.HIGH,
        keyRisks     = listOf(
            "Blocco dell'attivazione delle cellule T → risposta vaccinale ridotta",
            "Maggiore suscettibilità alle infezioni respiratorie",
            "Rischio di riattivazione della tubercolosi"
        ),
        generalNote  = "Abatacept blocca la co-stimolazione CD80/CD86-CD28 necessaria per la piena attivazione delle cellule T. La risposta vaccinale è attenuata ma presente e giustifica la vaccinazione. I pazienti mantengono una certa immunità cellulo-mediata.",
        preStartNote = "Vaccinare preferibilmente ≥4 settimane prima di iniziare abatacept. Screening per tubercolosi obbligatorio. Evitare vaccini vivi durante il trattamento e fino a 3 mesi dopo la sospensione."
    ),

    BiologicType.JAK_INHIBITOR to BiologicProfile(
        immunoLevel  = ImmunoLevel.HIGH,
        keyRisks     = listOf(
            "Rischio molto elevato di herpes zoster (3–4 volte superiore agli anti-TNF)",
            "Maggiore incidenza di infezioni opportunistiche rispetto agli altri biologici",
            "Rischio aumentato di tromboembolismo venoso",
            "Rischio cardiovascolare aumentato (tofacitinib/baricitinib in pazienti ≥50 anni)",
            "Riattivazione di tubercolosi e altre infezioni latenti"
        ),
        generalNote  = "I JAK inibitori bloccano vie di segnalazione intracellulare condivise da multiple citochine, producendo un'immunosoppressione ampia. Il rischio di herpes zoster è il più alto tra tutti i biologici: Shingrix è il vaccino più urgente.",
        preStartNote = "Screening obbligatorio per tubercolosi e infezioni virali latenti. Somministrare Shingrix idealmente prima di iniziare. Screening cardiovascolare se ≥50 anni o fattori di rischio presenti."
    ),

    BiologicType.VEGF_INHIBITOR to BiologicProfile(
        immunoLevel  = ImmunoLevel.MODERATE,
        keyRisks     = listOf(
            "Alterazione della cicatrizzazione e rischio di infezione delle ferite",
            "Immunosoppressione aggiuntiva da chemioterapia concomitante (frequente)",
            "Rischio di infezioni opportunistiche dipendente dal regime oncologico"
        ),
        generalNote  = "Gli anti-VEGF hanno un effetto immunosoppressivo modesto da soli, ma quasi sempre vengono usati insieme a chemioterapia che produce invece un'immunosoppressione significativa. Le raccomandazioni devono considerare il regime oncologico completo.",
        preStartNote = "Valutare la vaccinazione prima dell'inizio del trattamento oncologico. La chemioterapia concomitante può rendere inefficaci i vaccini somministrati durante il trattamento."
    ),

    BiologicType.BLYS_INHIBITOR to BiologicProfile(
        immunoLevel  = ImmunoLevel.HIGH,
        keyRisks     = listOf(
            "Riduzione delle cellule B e della produzione di anticorpi",
            "Maggiore suscettibilità alle infezioni respiratorie",
            "Rischio di riattivazione di infezioni latenti"
        ),
        generalNote  = "Belimumab inibisce la sopravvivenza delle cellule B autoreattive. L'effetto è meno profondo rispetto a rituximab, ma riduce la capacità di risposta umorale. La vaccinazione prima dell'inizio è preferibile ma non sempre fattibile.",
        preStartNote = "Vaccinare idealmente prima di iniziare. Se già in trattamento, i vaccini mantengono un'efficacia parziale e sono comunque indicati. Screening per HBV prima di iniziare."
    ),

    BiologicType.GUT_SELECTIVE to BiologicProfile(
        immunoLevel  = ImmunoLevel.LOW,
        keyRisks     = listOf(
            "Immunosoppressione sistemica minima (azione prevalentemente intestinale)",
            "Rischio di infezioni gastrointestinali",
            "Natalizumab: rischio di leucoencefalopatia multifocale progressiva (virus JC)"
        ),
        generalNote  = "Vedolizumab agisce selettivamente sull'intestino con scarsa immunosoppressione sistemica. Il rischio di infezioni extraintestinali è basso. La vaccinazione completa è comunque raccomandata per la patologia infiammatoria di base e i trattamenti concomitanti abituali.",
        preStartNote = "Per natalizumab: determinare gli anticorpi anti-JC prima di iniziare (rischio di LMP). I vaccini inattivati possono essere somministrati senza restrizioni di tempo. Con vedolizumab i vaccini vivi sono probabilmente sicuri, ma per precauzione si raccomanda di somministrarli prima dell'inizio o di evitarli."
    )
)

// ── Catalogo base dei vaccini ─────────────────────────────────────────────────

private data class BaseVaccine(
    val name: String,
    val brand: String? = null,
    val type: VaccineType,
    val defaultStatus: VaccineStatus,
    val defaultReason: String,
    val defaultTiming: String? = null,
    val defaultPriority: VaccinePriority = VaccinePriority.ROUTINE
)

private val BASE_VACCINES = listOf(
    BaseVaccine(
        name = "Influenza (vaccino inattivato o ricombinante)",
        brand = "Fluzone, Fluarix, Flucelvax",
        type = VaccineType.INACTIVATED,
        defaultStatus = VaccineStatus.RECOMMENDED,
        defaultReason = "Raccomandata annualmente a tutti i pazienti in terapia immunosoppressiva.",
        defaultTiming = "Ogni anno, idealmente in autunno prima della stagione influenzale",
        defaultPriority = VaccinePriority.ESSENTIAL
    ),
    BaseVaccine(
        name = "Influenza (spray nasale vivo attenuato)",
        brand = "FluMist",
        type = VaccineType.LIVE,
        defaultStatus = VaccineStatus.CONTRAINDICATED,
        defaultReason = "Vaccino vivo attenuato. Controindicato nei pazienti immunocompromessi. Usare sempre la formulazione inattivata.",
        defaultPriority = VaccinePriority.ESSENTIAL
    ),
    BaseVaccine(
        name = "Pneumococcico coniugato (PCV15 o PCV20)",
        brand = "Prevnar 15, Prevnar 20",
        type = VaccineType.SUBUNIT,
        defaultStatus = VaccineStatus.RECOMMENDED,
        defaultReason = "Tutti gli adulti immunocompromessi devono ricevere la vaccinazione pneumococcica coniugata per prevenire polmonite e malattia invasiva.",
        defaultTiming = "PCV15 seguita da PPSV23 a 8 settimane, oppure PCV20 da sola se non precedentemente vaccinato",
        defaultPriority = VaccinePriority.ESSENTIAL
    ),
    BaseVaccine(
        name = "Pneumococcico polisaccaridico (PPSV23)",
        brand = "Pneumovax 23",
        type = VaccineType.SUBUNIT,
        defaultStatus = VaccineStatus.RECOMMENDED,
        defaultReason = "Amplia la copertura sierotipica dopo PCV15. Importante negli immunocompromessi.",
        defaultTiming = "8 settimane dopo PCV15",
        defaultPriority = VaccinePriority.HIGH
    ),
    BaseVaccine(
        name = "COVID-19 (mRNA o subunità proteica)",
        brand = "Comirnaty, Spikevax, Nuvaxovid",
        type = VaccineType.MRNA,
        defaultStatus = VaccineStatus.RECOMMENDED,
        defaultReason = "Fortemente raccomandato negli immunocompromessi; ciclo primario completo più dosi di richiamo secondo le linee guida vigenti.",
        defaultPriority = VaccinePriority.ESSENTIAL
    ),
    BaseVaccine(
        name = "Epatite B",
        brand = "Engerix-B, Recombivax HB, Heplisav-B",
        type = VaccineType.RECOMBINANT,
        defaultStatus = VaccineStatus.RECOMMENDED,
        defaultReason = "Obbligatoria nei non immuni prima di iniziare il biologico. Molti biologici favoriscono la riattivazione dell'HBV.",
        defaultTiming = "Screening previo (HBsAg, anti-HBc, anti-HBs). Schema a 3 dosi o Heplisav-B in 2 dosi.",
        defaultPriority = VaccinePriority.HIGH
    ),
    BaseVaccine(
        name = "Epatite A",
        brand = "Havrix, Vaqta",
        type = VaccineType.INACTIVATED,
        defaultStatus = VaccineStatus.RECOMMENDED,
        defaultReason = "Raccomandata nei non immuni, in particolare con epatopatia o MICI.",
        defaultTiming = "2 dosi a distanza di 6–12 mesi",
        defaultPriority = VaccinePriority.ROUTINE
    ),
    BaseVaccine(
        name = "Td / Tdap (Tetano-Difterite-Pertosse)",
        brand = "Boostrix, Adacel",
        type = VaccineType.TOXOID,
        defaultStatus = VaccineStatus.RECOMMENDED,
        defaultReason = "Richiamo ogni 10 anni. Tdap almeno una volta se non precedentemente somministrato.",
        defaultPriority = VaccinePriority.ROUTINE
    ),
    BaseVaccine(
        name = "Herpes Zoster ricombinante (Shingrix)",
        brand = "Shingrix",
        type = VaccineType.RECOMBINANT,
        defaultStatus = VaccineStatus.RECOMMENDED,
        defaultReason = "Vaccino a subunità adiuvantato; sicuro negli immunocompromessi. I biologici aumentano significativamente il rischio di herpes zoster.",
        defaultTiming = "2 dosi a distanza di 2–6 mesi. Idealmente prima di iniziare il biologico.",
        defaultPriority = VaccinePriority.ESSENTIAL
    ),
    BaseVaccine(
        name = "Herpes Zoster vivo (Zostavax)",
        brand = "Zostavax",
        type = VaccineType.LIVE,
        defaultStatus = VaccineStatus.CONTRAINDICATED,
        defaultReason = "Vaccino vivo attenuato. Controindicato in terapia biologica. Usare sempre Shingrix (ricombinante)."
    ),
    BaseVaccine(
        name = "MPR — Morbillo-Parotite-Rosolia",
        brand = "M-M-R II, Priorix",
        type = VaccineType.LIVE,
        defaultStatus = VaccineStatus.CONTRAINDICATED,
        defaultReason = "Vaccino vivo attenuato. Controindicato durante la terapia biologica. Se il paziente non è immune, valutare la somministrazione ≥4 settimane prima dell'inizio.",
        defaultTiming = "Se necessario: ≥4 settimane prima di iniziare il biologico"
    ),
    BaseVaccine(
        name = "Varicella",
        brand = "Varivax",
        type = VaccineType.LIVE,
        defaultStatus = VaccineStatus.CONTRAINDICATED,
        defaultReason = "Vaccino vivo attenuato. Controindicato durante la terapia biologica. Nei soggetti suscettibili, somministrare ≥4 settimane prima dell'inizio.",
        defaultTiming = "Se necessario: ≥4 settimane prima di iniziare il biologico"
    ),
    BaseVaccine(
        name = "Meningococcico ACWY",
        brand = "Menactra, Menveo, MenQuadfi",
        type = VaccineType.SUBUNIT,
        defaultStatus = VaccineStatus.RECOMMENDED,
        defaultReason = "Raccomandato negli adulti immunocompromessi; essenziale in caso di asplenia o deficit del complemento.",
        defaultPriority = VaccinePriority.ROUTINE
    ),
    BaseVaccine(
        name = "Meningococcico B",
        brand = "Bexsero, Trumenba",
        type = VaccineType.RECOMBINANT,
        defaultStatus = VaccineStatus.RECOMMENDED,
        defaultReason = "Raccomandato negli immunocompromessi, in particolare con asplenia o deficit del complemento.",
        defaultPriority = VaccinePriority.ROUTINE
    ),
    BaseVaccine(
        name = "HPV (Papillomavirus Umano)",
        brand = "Gardasil 9",
        type = VaccineType.RECOMBINANT,
        defaultStatus = VaccineStatus.RECOMMENDED,
        defaultReason = "Raccomandato fino a 26 anni; decisione condivisa tra 27–45 anni. Sicuro negli immunocompromessi.",
        defaultPriority = VaccinePriority.ROUTINE
    ),
    BaseVaccine(
        name = "Haemophilus influenzae tipo b (Hib)",
        brand = "ActHIB, PedvaxHIB",
        type = VaccineType.SUBUNIT,
        defaultStatus = VaccineStatus.RECOMMENDED,
        defaultReason = "Raccomandato in caso di asplenia o asplenia funzionale; da considerare negli altri immunocompromessi.",
        defaultPriority = VaccinePriority.ROUTINE
    ),
    BaseVaccine(
        name = "Febbre gialla",
        brand = "YF-Vax, Stamaril",
        type = VaccineType.LIVE,
        defaultStatus = VaccineStatus.CONTRAINDICATED,
        defaultReason = "Vaccino vivo attenuato. Controindicato negli immunocompromessi. In caso di viaggio indispensabile in zona endemica, valutare il rapporto rischio-beneficio con lo specialista."
    ),
    BaseVaccine(
        name = "BCG (tubercolosi)",
        brand = "Vaccino BCG",
        type = VaccineType.LIVE,
        defaultStatus = VaccineStatus.CONTRAINDICATED,
        defaultReason = "Vaccino micobatterico vivo. Assolutamente controindicato in terapia biologica."
    ),
    BaseVaccine(
        name = "Febbre tifoide orale (Vivotif)",
        brand = "Vivotif",
        type = VaccineType.LIVE,
        defaultStatus = VaccineStatus.CONTRAINDICATED,
        defaultReason = "Batterio vivo attenuato. Controindicato. Usare la formulazione iniettabile (Typhim Vi) se necessaria la protezione."
    ),
    BaseVaccine(
        name = "Febbre tifoide iniettabile",
        brand = "Typhim Vi",
        type = VaccineType.INACTIVATED,
        defaultStatus = VaccineStatus.RECOMMENDED,
        defaultReason = "Opzione inattivata sicura per i viaggiatori in regioni endemiche.",
        defaultPriority = VaccinePriority.ROUTINE
    )
)

// ── Sovrascritture specifiche per biologico ───────────────────────────────────

private data class VaccineOverride(
    val priority: VaccinePriority? = null,
    val status: VaccineStatus? = null,
    val extraReason: String,
    val extraTiming: String? = null
)

private fun biologicOverrides(biologic: BiologicType): Map<String, VaccineOverride> = when (biologic) {

    BiologicType.TNF_INHIBITOR -> mapOf(
        "Epatite B" to VaccineOverride(
            priority = VaccinePriority.ESSENTIAL,
            extraReason = "Gli anti-TNF causano riattivazione dell'HBV fino al 39% dei portatori senza profilassi. Screening sierologico OBBLIGATORIO prima di iniziare. Vaccinare se suscettibile (anti-HBs < 10 mUI/mL). In caso di portatore: profilassi antivirale.",
            extraTiming = "Screening: HBsAg + anti-HBc + anti-HBs. Vaccinare ≥2 settimane prima dell'inizio se il tempo lo permette."
        ),
        "Herpes Zoster ricombinante (Shingrix)" to VaccineOverride(
            priority = VaccinePriority.ESSENTIAL,
            extraReason = "Gli anti-TNF raddoppiano il rischio di herpes zoster rispetto alla popolazione generale. Shingrix è efficace e sicuro in questo contesto."
        ),
        "Pneumococcico coniugato (PCV15 o PCV20)" to VaccineOverride(
            priority = VaccinePriority.ESSENTIAL,
            extraReason = "I pazienti con artrite reumatoide o altre malattie infiammatorie hanno un rischio maggiore di polmonite pneumococcica, amplificato dagli anti-TNF."
        ),
        "Influenza (vaccino inattivato o ricombinante)" to VaccineOverride(
            priority = VaccinePriority.ESSENTIAL,
            extraReason = "Vaccinazione annuale obbligatoria. Gli anti-TNF non riducono in modo significativo la risposta al vaccino antinfluenzale inattivato."
        )
    )

    BiologicType.IL6_INHIBITOR -> mapOf(
            "Influenza (vaccino inattivato o ricombinante)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "PRIORITARIA: il blocco dell'IL-6 maschera la febbre e i reattanti di fase acuta. L'influenza può progredire a polmonite grave senza sintomi d'allarme evidenti. La vaccinazione è particolarmente critica in questo contesto.",
                extraTiming = "Vaccinare prima dell'inizio del trattamento se possibile; la risposta vaccinale è migliore prima dell'immunosoppressione."
            ),
            "COVID-19 (mRNA o subunità proteica)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "Il mascheramento infiammatorio da anti-IL-6 può far progredire il COVID-19 in modo atipico e grave. La vaccinazione è particolarmente urgente."
            ),
            "Epatite B" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "Rischio di riattivazione dell'HBV. Screening sierologico obbligatorio prima di iniziare tocilizumab o sarilumab.",
                extraTiming = "HBsAg + anti-HBc + anti-HBs prima di iniziare. Profilassi antivirale se portatore."
            ),
            "Pneumococcico coniugato (PCV15 o PCV20)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "Rischio elevato di polmonite batterica per soppressione dell'IL-6, citochina essenziale nella risposta ai batteri capsulati."
            )
        )

    BiologicType.IL17_INHIBITOR -> mapOf(
            "Pneumococcico coniugato (PCV15 o PCV20)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "L'IL-17 interviene nella difesa mucosale contro i batteri extracellulari. Il suo blocco aumenta il rischio di infezioni respiratorie, in particolare pneumococciche."
            ),
            "Influenza (vaccino inattivato o ricombinante)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "Raccomandata annualmente. La risposta vaccinale è meno compromessa rispetto agli anti-TNF o anti-IL-6."
            ),
            "Epatite B" to VaccineOverride(
                priority = VaccinePriority.HIGH,
                extraReason = "Rischio di riattivazione minore rispetto agli anti-TNF, ma lo screening prima dell'inizio rimane raccomandato."
            ),
            "Herpes Zoster ricombinante (Shingrix)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "Rischio aumentato di herpes zoster, benché inferiore rispetto a JAKi o anti-TNF. Shingrix raccomandata negli adulti ≥50 anni."
            )
        )

    BiologicType.IL12_23_INHIBITOR -> mapOf(
            "Epatite B" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "Screening per HBV obbligatorio prima di iniziare ustekinumab. Vaccinare se suscettibile.",
                extraTiming = "HBsAg + anti-HBc + anti-HBs prima di iniziare."
            ),
            "Herpes Zoster ricombinante (Shingrix)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "IL-12 e IL-23 sono fondamentali per il controllo delle infezioni intracellulari, incluso VZV. Shingrix raccomandata prima di iniziare."
            ),
            "Influenza (vaccino inattivato o ricombinante)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "Raccomandata annualmente. La risposta vaccinale è generalmente preservata con ustekinumab."
            )
        )

    BiologicType.IL23_INHIBITOR -> mapOf(
            "Influenza (vaccino inattivato o ricombinante)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "Raccomandata annualmente. Il profilo di immunosoppressione è favorevole e la risposta ai vaccini è ben preservata con gli inibitori selettivi di IL-23."
            ),
            "Pneumococcico coniugato (PCV15 o PCV20)" to VaccineOverride(
                priority = VaccinePriority.HIGH,
                extraReason = "Raccomandata anche se il rischio di infezione grave con IL-23i è basso. La malattia infiammatoria di base giustifica la vaccinazione completa."
            ),
            "Herpes Zoster ricombinante (Shingrix)" to VaccineOverride(
                priority = VaccinePriority.HIGH,
                extraReason = "Il rischio di zoster è il più basso tra i biologici con inibitori selettivi di IL-23, ma Shingrix è raccomandata negli adulti ≥50 anni."
            )
        )

    BiologicType.B_CELL_DEPLETER -> mapOf(
            "Epatite B" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "CRITICO: rituximab può riattivare l'HBV in modo fulminante anche in pazienti anti-HBc+ con HBsAg negativo. Screening sierologico OBBLIGATORIO. Profilassi antivirale in tutti i portatori e anti-HBc+ prima di iniziare.",
                extraTiming = "HBsAg + anti-HBc + anti-HBs PRIMA di ogni ciclo. Se positivo: profilassi con entecavir o tenofovir."
            ),
            "Influenza (vaccino inattivato o ricombinante)" to VaccineOverride(
                status = VaccineStatus.CAUTION,
                extraReason = "RISPOSTA RIDOTTA O NULLA se somministrata durante il trattamento attivo con rituximab (deplezione B completa). Il vaccino è comunque sicuro ma la sua efficacia è molto limitata. Vaccinare idealmente prima dell'inizio o ≥6 mesi dopo l'ultima infusione.",
                extraTiming = "Finestra ottimale: ≥4 settimane PRIMA della prossima infusione oppure ≥6 mesi dopo l'ultima."
            ),
            "COVID-19 (mRNA o subunità proteica)" to VaccineOverride(
                status = VaccineStatus.CAUTION,
                extraReason = "La risposta anticorpale dopo il vaccino COVID-19 può essere non rilevabile durante il trattamento con rituximab. Considerare la vaccinazione prima dell'inizio. L'immunità cellulo-mediata T può essere parzialmente preservata.",
                extraTiming = "Vaccinare ≥4 settimane prima del prossimo ciclo o ≥6 mesi dopo l'ultimo."
            ),
            "Pneumococcico coniugato (PCV15 o PCV20)" to VaccineOverride(
                status = VaccineStatus.CAUTION,
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "ESSENZIALE ma con EFFICACIA COMPROMESSA durante il trattamento attivo. L'ipogammaglobulinemia progressiva aumenta il rischio di infezioni da germi capsulati. Vaccinare prima dell'inizio.",
                extraTiming = "Somministrare ≥4 settimane prima dell'inizio o ≥6 mesi dopo l'ultima infusione."
            ),
            "Herpes Zoster ricombinante (Shingrix)" to VaccineOverride(
                status = VaccineStatus.CAUTION,
                extraReason = "Shingrix è un vaccino a subunità (sicuro), ma la risposta può essere attenuata durante la deplezione B. Si raccomanda di somministrarlo prima dell'inizio del trattamento.",
                extraTiming = "Preferibilmente ≥4 settimane prima dell'inizio di rituximab."
            )
        )

    BiologicType.T_CELL_COSTIM -> mapOf(
            "Herpes Zoster ricombinante (Shingrix)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "Abatacept riduce l'attivazione delle cellule T necessaria per controllare il VZV latente. Shingrix è efficace e sicuro; somministrare idealmente prima dell'inizio.",
                extraTiming = "Preferibilmente ≥4 settimane prima della prima dose di abatacept."
            ),
            "Influenza (vaccino inattivato o ricombinante)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "Raccomandata annualmente. La risposta vaccinale può essere attenuata ma è sufficientemente protettiva. Vaccinare prima dell'inizio ove possibile."
            ),
            "Epatite B" to VaccineOverride(
                priority = VaccinePriority.HIGH,
                extraReason = "Screening obbligatorio prima di iniziare. Rischio di riattivazione moderato con abatacept.",
                extraTiming = "HBsAg + anti-HBc + anti-HBs prima di iniziare."
            ),
            "MPR — Morbillo-Parotite-Rosolia" to VaccineOverride(
                extraReason = "Controindicato durante il trattamento. Evitare anche fino a 3 mesi dopo la sospensione di abatacept.",
                extraTiming = "Sospendere abatacept ≥3 mesi prima se si rende necessario un vaccino vivo (situazioni eccezionali)."
            )
        )

    BiologicType.JAK_INHIBITOR -> mapOf(
            "Herpes Zoster ricombinante (Shingrix)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "MASSIMA PRIORITÀ: i JAK inibitori triplicano o quadruplicano il rischio di herpes zoster rispetto agli anti-TNF. Shingrix deve essere somministrata PRIMA di iniziare il JAKi ove possibile. È il vaccino più urgente in questo contesto.",
                extraTiming = "PRIMA di iniziare il JAKi. Se già in trattamento, può essere somministrata ugualmente poiché Shingrix è ricombinante (non viva)."
            ),
            "Influenza (vaccino inattivato o ricombinante)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "I JAKi producono un'immunosoppressione ampia per blocco di multiple citochine. L'influenza è particolarmente pericolosa. Vaccinazione annuale obbligatoria.",
                extraTiming = "Annualmente. La risposta vaccinale può essere lievemente ridotta; la vaccinazione rimane comunque raccomandata."
            ),
            "COVID-19 (mRNA o subunità proteica)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "I JAKi riducono la risposta ai vaccini mRNA. Ciclo primario completo più richiami periodici. Alcuni esperti suggeriscono di sospendere temporaneamente il JAKi (se clinicamente possibile) intorno alla vaccinazione."
            ),
            "Pneumococcico coniugato (PCV15 o PCV20)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "Rischio elevato di infezioni respiratorie batteriche con JAKi per soppressione delle vie JAK/STAT coinvolte nella difesa contro i batteri extracellulari."
            ),
            "Epatite B" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "I JAKi possono riattivare infezioni latenti incluso HBV. Screening sierologico OBBLIGATORIO prima di iniziare.",
                extraTiming = "HBsAg + anti-HBc + anti-HBs prima di iniziare. Se portatore: profilassi antivirale."
            )
        )

    BiologicType.VEGF_INHIBITOR -> mapOf(
            "Influenza (vaccino inattivato o ricombinante)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "Prioritaria, in particolare se è presente chemioterapia concomitante. La combinazione bevacizumab + chemioterapia può determinare un'immunosoppressione significativa."
            ),
            "Pneumococcico coniugato (PCV15 o PCV20)" to VaccineOverride(
                priority = VaccinePriority.HIGH,
                extraReason = "Il contesto oncologico e la chemioterapia associata aumentano il rischio di infezione pneumococcica. Vaccinare prima di iniziare il trattamento ove possibile."
            )
        )

    BiologicType.BLYS_INHIBITOR -> mapOf(
            "Epatite B" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "Belimumab riduce le cellule B e può riattivare l'HBV nei portatori. Screening OBBLIGATORIO prima di iniziare.",
                extraTiming = "HBsAg + anti-HBc + anti-HBs prima di iniziare."
            ),
            "Influenza (vaccino inattivato o ricombinante)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "Raccomandata annualmente. Somministrare alcune settimane prima della successiva dose di belimumab per massimizzare la risposta vaccinale.",
                extraTiming = "Idealmente alcune settimane prima della prossima dose di belimumab."
            ),
            "Pneumococcico coniugato (PCV15 o PCV20)" to VaccineOverride(
                priority = VaccinePriority.HIGH,
                extraReason = "La riduzione delle cellule B può compromettere la risposta ai vaccini T-indipendenti come i polisaccaridici. I coniugati garantiscono una risposta migliore."
            )
        )

    BiologicType.GUT_SELECTIVE -> mapOf(
            "Influenza (vaccino inattivato o ricombinante)" to VaccineOverride(
                priority = VaccinePriority.ESSENTIAL,
                extraReason = "Raccomandata annualmente. Vedolizumab ha minima immunosoppressione sistemica; la risposta vaccinale è ben preservata."
            ),
            "MPR — Morbillo-Parotite-Rosolia" to VaccineOverride(
                extraReason = "Con vedolizumab l'immunosoppressione sistemica è minima; alcuni esperti ritengono che i vaccini vivi possano essere sicuri, ma le linee guida attuali raccomandano cautela. Somministrare prima dell'inizio se il paziente non è immune. Con natalizumab mantenere le stesse precauzioni degli altri biologici.",
                extraTiming = "Se necessario: somministrare prima di iniziare e discutere il caso con lo specialista."
            ),
            "Varicella" to VaccineOverride(
                extraReason = "Analogamente all'MPR: con vedolizumab potrebbe essere accettabile in determinate circostanze (scarsa immunosoppressione sistemica), ma si raccomanda di somministrarlo prima dell'inizio per precauzione. Consultare lo specialista.",
                extraTiming = "Preferibilmente prima dell'inizio del trattamento."
            ),
            "Epatite A" to VaccineOverride(
                priority = VaccinePriority.HIGH,
                extraReason = "Particolarmente importante nelle MICI (colite ulcerosa, Crohn) per il rischio di epatite A grave in presenza di epatopatia sottostante e coinvolgimento del tubo digerente infiammato."
            )
        )
}

// ── Motore delle raccomandazioni ──────────────────────────────────────────────

fun getVaccineRecommendations(
    sex: Sex,
    biologic: BiologicType,
    conditions: Set<MedicalCondition>
): List<VaccineRec> {
    val overrides = biologicOverrides(biologic)
    return BASE_VACCINES.map { base ->
        val override = overrides[base.name]
        var rec = VaccineRec(
            name     = base.name,
            brand    = base.brand,
            type     = base.type,
            status   = override?.status ?: base.defaultStatus,
            reason   = buildReason(base.defaultReason, override?.extraReason),
            timing   = buildTiming(base.defaultTiming, override?.extraTiming),
            priority = override?.priority ?: base.defaultPriority
        )
        rec = applyConditionModifiers(rec, conditions)
        rec = applySexModifiers(rec, sex)
        rec
    }
}

private fun buildReason(base: String, extra: String?): String =
    if (extra != null) "$base\n\n$extra" else base

private fun buildTiming(base: String?, extra: String?): String? = when {
    base != null && extra != null -> "$base — $extra"
    extra != null                 -> extra
    else                          -> base
}

// ── Modificatori per condizione clinica ───────────────────────────────────────

private fun applyConditionModifiers(v: VaccineRec, conditions: Set<MedicalCondition>): VaccineRec {
    var u = v

    if (conditions.contains(MedicalCondition.ASPLENIA)) {
        if (u.name.contains("pneumococcic", ignoreCase = true) ||
            u.name.contains("meningococcic", ignoreCase = true) ||
            u.name.contains("Haemophilus", ignoreCase = true)
        ) {
            u = u.copy(priority = VaccinePriority.ESSENTIAL, reason = "[ASPLENIA — ESSENZIALE] ${u.reason}")
        }
    }

    if (conditions.contains(MedicalCondition.CHRONIC_KIDNEY_DISEASE) &&
        u.name.contains("Epatite B", ignoreCase = true) &&
        u.status == VaccineStatus.RECOMMENDED
    ) {
        u = u.copy(
            priority = VaccinePriority.ESSENTIAL,
            reason   = "${u.reason}\n\n[IRC] Utilizzare la formulazione ad alto dosaggio (40 mcg). Verificare la sieroproptezione (anti-HBs ≥10 mUI/mL) e somministrare dose aggiuntiva se necessario."
        )
    }

    if (conditions.contains(MedicalCondition.LIVER_DISEASE)) {
        if (u.name.contains("Epatite A", ignoreCase = true) || u.name.contains("Epatite B", ignoreCase = true)) {
            u = u.copy(priority = VaccinePriority.ESSENTIAL, reason = "[EPATOPATIA — ESSENZIALE] ${u.reason}")
        }
    }

    if (conditions.contains(MedicalCondition.PREGNANCY)) {
        if (u.name.contains("Td /", ignoreCase = true) || u.name.contains("Tdap", ignoreCase = true)) {
            u = u.copy(
                priority = VaccinePriority.ESSENTIAL,
                reason   = "${u.reason}\n\n[GRAVIDANZA] Tdap raccomandata in ogni gravidanza (27–36 settimane) per proteggere il neonato dalla pertosse."
            )
        }
        if (u.name.contains("Influenza", ignoreCase = true) && u.status == VaccineStatus.RECOMMENDED) {
            u = u.copy(
                priority = VaccinePriority.ESSENTIAL,
                reason   = "${u.reason}\n\n[GRAVIDANZA] Particolarmente importante in gravidanza. L'influenza nelle gestanti può causare complicanze gravi."
            )
        }
        if (u.type == VaccineType.LIVE && u.status == VaccineStatus.CONTRAINDICATED) {
            u = u.copy(reason = "${u.reason}\n\n[GRAVIDANZA] Doppiamente controindicato in gravidanza.")
        }
    }

    return u
}

private fun applySexModifiers(v: VaccineRec, sex: Sex): VaccineRec {
    if (sex == Sex.MALE && v.name.contains("HPV", ignoreCase = true)) {
        return v.copy(reason = "${v.reason} Raccomandato nei maschi fino a 26 anni (incluso nel calendario vaccinale in molte regioni italiane).")
    }
    return v
}
