package com.example.progetto_7_vaccini.data.database

import com.example.progetto_7_vaccini.data.database.entities.*
import com.example.progetto_7_vaccini.data.DateUtils
import java.io.Serializable

// ── Modelli di dominio (Migrati da VaccineData) ──────────────────────────────

enum class VaccineStatus { RECOMMENDED, CONTRAINDICATED, CAUTION, ALREADY_DONE }
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
) : Serializable

data class BiologicProfile(
    val immunoLevel: ImmunoLevel,
    val keyRisks: List<String>,
    val generalNote: String,
    val preStartNote: String? = null
) : Serializable

class MotoreDecisionale {

    // ── Catalogo base dei vaccini ───────────────────────────────────────────────

    data class BaseVaccine(
        val name: String,
        val brand: String? = null,
        val type: VaccineType,
        val defaultStatus: VaccineStatus,
        val defaultReason: String,
        val defaultTiming: String? = null,
        val defaultPriority: VaccinePriority = VaccinePriority.ROUTINE
    )

    companion object {
        val BASE_VACCINES = listOf(
            BaseVaccine(
                name = "Influenza (inattivato)",
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
                name = "Pneumococco PCV20",
                brand = "Prevnar 15, Prevnar 20",
                type = VaccineType.SUBUNIT,
                defaultStatus = VaccineStatus.RECOMMENDED,
                defaultReason = "Tutti gli adulti immunocompromessi devono ricevere la vaccinazione pneumococcica coniugata per prevenire polmonite e malattia invasiva.",
                defaultTiming = "PCV15 seguita da PPSV23 a 8 settimane, oppure PCV20 da sola se non precedentemente vaccinato",
                defaultPriority = VaccinePriority.ESSENTIAL
            ),
            BaseVaccine(
                name = "Pneumococco PPSV23",
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
    }


    // ── Sovrascritture specifiche per biologico ────────────────────────────────

    private data class VaccineOverride(
        val priority: VaccinePriority? = null,
        val status: VaccineStatus? = null,
        val extraReason: String,
        val extraTiming: String? = null
    )

    private fun getOverridesForBiologic(biologicName: String): Map<String, VaccineOverride> {
        val name = biologicName.uppercase().trim()
        return when {
            name.contains("TNF") -> mapOf(
                "Epatite B" to VaccineOverride(
                    priority = VaccinePriority.ESSENTIAL,
                    extraReason = "Gli anti-TNF causano riattivazione dell'HBV fino al 39% dei portatori senza profilassi. Screening sierologico OBBLIGATORIO prima di iniziare. Vaccinare se suscettibile (anti-HBs < 10 mUI/mL). In caso di portatore: profilassi antivirale.",
                    extraTiming = "Screening: HBsAg + anti-HBc + anti-HBs. Vaccinare ≥2 settimane prima dell'inizio se il tempo lo permette."
                ),
                "Herpes Zoster ricombinante (Shingrix)" to VaccineOverride(
                    priority = VaccinePriority.ESSENTIAL,
                    extraReason = "Gli anti-TNF raddoppiano il rischio di herpes zoster rispetto alla popolazione generale. Shingrix è efficace e sicuro in questo contesto."
                ),
                "Pneumococco PCV20" to VaccineOverride(
                    priority = VaccinePriority.ESSENTIAL,
                    extraReason = "I pazienti con artrite reumatoide o altre malattie infiammatorie hanno un rischio maggiore di polmonite pneumococcica, amplificato dagli anti-TNF."
                ),
                "Influenza (inattivato)" to VaccineOverride(
                    priority = VaccinePriority.ESSENTIAL,
                    extraReason = "Vaccinazione annuale obbligatoria. Gli anti-TNF non riducono in modo significativo la risposta al vaccino antinfluenzale inattivato."
                )
            )
            name.contains("IL-6") || name.contains("IL6") -> mapOf(
                "Influenza (inattivato)" to VaccineOverride(
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
                "Pneumococco PCV20" to VaccineOverride(
                    priority = VaccinePriority.ESSENTIAL,
                    extraReason = "Rischio elevato di polmonite batterica per soppressione dell'IL-6, citochina essenziale nella risposta ai batteri capsulati."
                )
            )
            name.contains("IL-17") || name.contains("IL17") -> mapOf(
                "Pneumococco PCV20" to VaccineOverride(
                    priority = VaccinePriority.ESSENTIAL,
                    extraReason = "L'IL-17 interviene nella difesa mucosale anticancer i batteri extracellulari. Il suo blocco aumenta il rischio di infezioni respiratorie, in particolare pneumococciche."
                ),
                "Influenza (inattivato)" to VaccineOverride(
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
            name.contains("IL-12") || name.contains("IL12") || name.contains("IL-23") || name.contains("IL23") -> {
                // IL-12/23 e IL-23 (condivisa o molto simile)
                mapOf(
                    "Epatite B" to VaccineOverride(
                        priority = VaccinePriority.ESSENTIAL,
                        extraReason = "Screening per HBV obbligatorio prima di iniziare ustekinumab o inibitori IL-23. Vaccinare se suscettibile.",
                        extraTiming = "HBsAg + anti-HBc + anti-HBs prima di iniziare."
                    ),
                    "Herpes Zoster ricombinante (Shingrix)" to VaccineOverride(
                        priority = VaccinePriority.ESSENTIAL,
                        extraReason = "IL-12 e IL-23 sono fondamentali per il controllo delle infezioni intracellulari, incluso VZV. Shingrix raccomandata prima di iniziare."
                    ),
                    "Influenza (inattivato)" to VaccineOverride(
                        priority = VaccinePriority.ESSENTIAL,
                        extraReason = "Raccomandata annualmente. La risposta vaccinale è generalmente preservata."
                    )
                )
            }
            name.contains("B-CELL") || name.contains("CD20") || name.contains("RITUXIMAB") || name.contains("DEPLETTORE") -> mapOf(
                "Epatite B" to VaccineOverride(
                    priority = VaccinePriority.ESSENTIAL,
                    extraReason = "CRITICO: rituximab può riattivare l'HBV in modo fulminante anche in pazienti anti-HBc+ con HBsAg negativo. Screening sierologico OBBLIGATORIO. Profilassi antivirale in tutti i portatori e anti-HBc+ prima di iniziare.",
                    extraTiming = "HBsAg + anti-HBc + anti-HBs PRIMA di ogni ciclo. Se positivo: profilassi con entecavir o tenofovir."
                ),
                "Influenza (inattivato)" to VaccineOverride(
                    status = VaccineStatus.CAUTION,
                    extraReason = "RISPOSTA RIDOTTA O NULLA se somministrata durante il trattamento attivo con rituximab (deplezione B completa). Il vaccino è comunque sicuro ma la sua efficacia è molto limitata. Vaccinare idealmente prima dell'inizio o ≥6 mesi dopo l'ultima infusione.",
                    extraTiming = "Finestra ottimale: ≥4 settimane PRIMA della prossima infusione oppure ≥6 mesi dopo l'ultima."
                ),
                "COVID-19 (mRNA o subunità proteica)" to VaccineOverride(
                    status = VaccineStatus.CAUTION,
                    extraReason = "La risposta anticorpale dopo il vaccino COVID-19 può essere non rilevabile durante il trattamento con rituximab. Considerare la vaccinazione prima dell'inizio. L'immunità cellulo-mediata T può essere parzialmente preservata.",
                    extraTiming = "Vaccinare ≥4 settimane prima del prossimo ciclo o ≥6 mesi dopo l'ultimo."
                ),
                "Pneumococco PCV20" to VaccineOverride(
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
            name.contains("T-CELL") || name.contains("ABATACEPT") || name.contains("CO-STIMOLAZIONE") -> mapOf(
                "Herpes Zoster ricombinante (Shingrix)" to VaccineOverride(
                    priority = VaccinePriority.ESSENTIAL,
                    extraReason = "Abatacept riduce l'attivazione delle cellule T necessaria per controllare il VZV latente. Shingrix è efficace e sicuro; somministrare idealmente prima dell'inizio.",
                    extraTiming = "Preferibilmente ≥4 settimane prima della prima dose di abatacept."
                ),
                "Influenza (inattivato)" to VaccineOverride(
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
            name.contains("JAK") -> mapOf(
                "Herpes Zoster ricombinante (Shingrix)" to VaccineOverride(
                    priority = VaccinePriority.ESSENTIAL,
                    extraReason = "MASSIMA PRIORITÀ: i JAK inibitori triplicano o quadruplicano il rischio di herpes zoster rispetto agli anti-TNF. Shingrix deve essere somministrata PRIMA di iniziare il JAKi ove possibile. È il vaccino più urgente in questo contesto.",
                    extraTiming = "PRIMA di iniziare il JAKi. Se già in trattamento, può essere somministrata ugualmente poiché Shingrix è ricombinante (non viva)."
                ),
                "Influenza (inattivato)" to VaccineOverride(
                    priority = VaccinePriority.ESSENTIAL,
                    extraReason = "I JAKi producono un'immunosoppressione ampia per blocco di multiple citochine. L'influenza è particolarmente pericolosa. Vaccinazione annuale obbligatoria.",
                    extraTiming = "Annualmente. La risposta vaccinale può essere lievemente ridotta; la vaccinazione rimane comunque raccomandata."
                ),
                "COVID-19 (mRNA o subunità proteica)" to VaccineOverride(
                    priority = VaccinePriority.ESSENTIAL,
                    extraReason = "I JAKi riducono la risposta ai vaccini mRNA. Ciclo primario completo più richiami periodici. Alcuni esperti suggeriscono di sospendere temporaneamente il JAKi (se clinicamente possibile) intorno alla vaccinazione."
                ),
                "Pneumococco PCV20" to VaccineOverride(
                    priority = VaccinePriority.ESSENTIAL,
                    extraReason = "Rischio elevato di infezioni respiratorie batteriche con JAKi per soppressione delle vie JAK/STAT coinvolte nella difesa contro i batteri extracellulari."
                ),
                "Epatite B" to VaccineOverride(
                    priority = VaccinePriority.ESSENTIAL,
                    extraReason = "I JAKi possono riattivare infezioni latenti incluso HBV. Screening sierologico OBBLIGATORIO prima di iniziare.",
                    extraTiming = "HBsAg + anti-HBc + anti-HBs prima di iniziare. Se portatore: profilassi antivirale."
                )
            )
            name.contains("VEGF") -> mapOf(
                "Influenza (inattivato)" to VaccineOverride(
                    priority = VaccinePriority.ESSENTIAL,
                    extraReason = "Prioritaria, in particolare se è presente chemioterapia concomitante. La combinazione bevacizumab + chemioterapia può determinare un'immunosoppressione significativa."
                ),
                "Pneumococco PCV20" to VaccineOverride(
                    priority = VaccinePriority.HIGH,
                    extraReason = "Il contesto oncologico e la chemioterapia associata aumentano il rischio di infezione pneumococcica. Vaccinare prima di iniziare il trattamento ove possibile."
                )
            )
            name.contains("BLYS") || name.contains("BELIMUMAB") -> mapOf(
                "Epatite B" to VaccineOverride(
                    priority = VaccinePriority.ESSENTIAL,
                    extraReason = "Belimumab riduce le cellule B e può riattivare l'HBV nei portatori. Screening OBBLIGATORIO prima di iniziare.",
                    extraTiming = "HBsAg + anti-HBc + anti-HBs prima di iniziare."
                ),
                "Influenza (inattivato)" to VaccineOverride(
                    priority = VaccinePriority.ESSENTIAL,
                    extraReason = "Raccomandata annualmente. Somministrare alcune settimane prima della successiva dose di belimumab per massimizzare la risposta vaccinale.",
                    extraTiming = "Idealmente alcune settimane prima della prossima dose di belimumab."
                ),
                "Pneumococco PCV20" to VaccineOverride(
                    priority = VaccinePriority.HIGH,
                    extraReason = "La riduzione delle cellule B può compromettere la risposta ai vaccini T-indipendenti come i polisaccaridici. I coniugati garantiscono una risposta migliore."
                )
            )
            name.contains("GUT") || name.contains("VEDOLIZUMAB") || name.contains("INTEGRINA") -> mapOf(
                "Influenza (inattivato)" to VaccineOverride(
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
            else -> emptyMap()
        }
    }

    // ── Motore Principale ────────────────────────────────────────────────────────

    suspend fun calcolaRaccomandazioniPerPaziente(
        database: AppDatabase,
        idPaziente: Long
    ): List<VaccineRec> {
        val paziente = database.pazienteDao().getPaziente(idPaziente) ?: return emptyList()
        
        // 1. Recupero Cura Biologica dal DB
        val curaDb = database.curaBiologicaDao().getCura(paziente.idCura)
        val biologicName = curaDb?.nome ?: ""
        
        // 2. Recupero Condizioni Cliniche dal DB
        val condPaziente = database.pazienteCondizioneDao().getCondizioniByPaziente(idPaziente)
        val tutteCondDb = database.condizioneClinicaDao().getTutteLeCondizioni()
        val conditions = condPaziente.mapNotNull { cp ->
            tutteCondDb.find { it.idCondizione == cp.idCondizione }
        }
        
        // 3. Recupero Storia Vaccinale dal DB
        val vaccPaziente = database.vaccinazioneDao().getVaccinazioniByPaziente(idPaziente)
        val tuttiVaccDb = database.vaccinoDao().getTuttiVaccini()
        val historyIds = vaccPaziente.map { it.idVaccino }.toSet()
        
        val age = DateUtils.calculateAge(paziente.dataNascita)
        val sexLabel = if (paziente.sesso == Sesso.MASCHIO) "Maschio" else "Femmina"
        
        val raccomandazioni = calcolaVolatile(
            sexLabel = sexLabel,
            biologicName = biologicName,
            age = age,
            selectedConditionIds = conditions.map { it.idCondizione }.toSet(),
            completedVaccineIds = historyIds,
            database = database
        )
        
        // 5. Salvataggio Esiti nel DB
        val raccomandazioneDao = database.raccomandazioneVaccinoDao()
        val tuttiVaccini = database.vaccinoDao().getTuttiVaccini()
        raccomandazioni.forEach { rec ->
            val idVaccino = tuttiVaccini.find { it.nome == rec.name }?.idVaccino ?: 0L
            if (idVaccino != 0L) {
                raccomandazioneDao.inserisciRaccomandazione(
                    RaccomandazioneVaccino(
                        idPaziente = idPaziente,
                        idVaccino = idVaccino,
                        esito = when(rec.status) {
                            VaccineStatus.RECOMMENDED -> EsitoVaccino.CONSENTITO
                            VaccineStatus.CONTRAINDICATED -> EsitoVaccino.CONTROINDICATO
                            VaccineStatus.CAUTION -> EsitoVaccino.VALUTARE
                            VaccineStatus.ALREADY_DONE -> EsitoVaccino.FATTO
                        }
                    )
                )
            }
        }
        
        return raccomandazioni
    }

    suspend fun calcolaVolatile(
        sexLabel: String,
        biologicName: String,
        age: Int?,
        selectedConditionIds: Set<Long>,
        completedVaccineIds: Set<Long>,
        database: AppDatabase
    ): List<VaccineRec> {
        val tutteCondDb = database.condizioneClinicaDao().getTutteLeCondizioni()
        val selectedConditions = tutteCondDb.filter { it.idCondizione in selectedConditionIds }

        val tuttiVacciniDb = database.vaccinoDao().getTuttiVaccini()
        val completedVaccineNames = tuttiVacciniDb.filter { it.idVaccino in completedVaccineIds }.map { it.nome }.toSet()

        val overrides = getOverridesForBiologic(biologicName)

        return BASE_VACCINES.map { base ->
            val override = overrides[base.name]
            var status = override?.status ?: base.defaultStatus

            var rec = VaccineRec(
                name     = base.name,
                brand    = base.brand,
                type     = base.type,
                status   = status,
                reason   = if (override?.extraReason != null) "${base.defaultReason}\n\n${override.extraReason}" else base.defaultReason,
                timing   = when {
                    base.defaultTiming != null && override?.extraTiming != null -> "${base.defaultTiming} — ${override.extraTiming}"
                    override?.extraTiming != null -> override.extraTiming
                    else -> base.defaultTiming
                },
                priority = override?.priority ?: base.defaultPriority
            )

            rec = applyConditionModifiers(rec, selectedConditions)

            if (sexLabel == "Maschio" && rec.name.contains("HPV", ignoreCase = true)) {
                rec = rec.copy(reason = "${rec.reason} Raccomandato nei maschi fino a 26 anni.")
            }

            if (age != null) {
                if (rec.name.contains("HPV", ignoreCase = true)) {
                    rec = when {
                        age <= 26 -> rec.copy(priority = VaccinePriority.HIGH)
                        age <= 45 -> rec.copy(priority = VaccinePriority.ROUTINE, reason = "${rec.reason}\n\n[ETÀ] Tra 27 e 45 anni la vaccinazione è frutto di decisione clinica condivisa.")
                        else -> rec.copy(status = VaccineStatus.CAUTION, reason = "Generalmente non raccomandato oltre i 45 anni.")
                    }
                }
                if (rec.name.contains("Shingrix", ignoreCase = true) && age >= 50) {
                    rec = rec.copy(priority = VaccinePriority.ESSENTIAL, reason = "${rec.reason}\n\n[ETÀ] Sopra i 50 anni il rischio aumenta drasticamente.")
                }
            }

            if (completedVaccineNames.contains(base.name)) {
                rec = VaccineRec(
                    name = rec.name,
                    brand = rec.brand,
                    type = rec.type,
                    status = VaccineStatus.ALREADY_DONE,
                    reason = rec.reason,
                    timing = rec.timing,
                    priority = rec.priority
                )
            }
            rec
        }
    }

    private fun applyConditionModifiers(v: VaccineRec, conditions: List<CondizioneClinica>): VaccineRec {
        var u = v
        val names = conditions.map { it.nome.uppercase() }

        // HIV
        if (names.any { it.contains("HIV") || it.contains("IMMUNODEFICIENZA") }) {
            if (u.type == VaccineType.LIVE) {
                u = u.copy(status = VaccineStatus.CONTRAINDICATED, reason = "[HIV/IMMUNODEFICIENZA] I vaccini vivi sono generalmente controindicati nei pazienti immunocompromessi.")
            } else if (u.name.contains("Influenza", ignoreCase = true) ||
                u.name.contains("Pneumococco", ignoreCase = true) ||
                u.name.contains("COVID", ignoreCase = true) ||
                u.name.contains("Epatite B", ignoreCase = true)) {
                u = u.copy(priority = VaccinePriority.ESSENTIAL, reason = "[HIV/IMMUNODEFICIENZA — ESSENZIALE] ${u.reason}\n\nRischio elevato di complicanze gravi in caso di infezione.")
            }
        }

        // DIABETE / MALATTIE CRONICHE
        if (names.any { it.contains("DIABETE") || it.contains("BPCO") || it.contains("POLMONARE") || it.contains("CARDIOPATIA") || it.contains("CUORE") || it.contains("POLMONI") }) {
            if (u.name.contains("Influenza", ignoreCase = true) || u.name.contains("Pneumococco", ignoreCase = true)) {
                u = u.copy(priority = VaccinePriority.ESSENTIAL, reason = "[MALATTIA CRONICA — ESSENZIALE] ${u.reason}\n\nRaccomandata per comorbidità.")
            }
        }

        if (names.any { it.contains("ASPLENIA") || it.contains("MILZA") }) {
            if (u.name.contains("Pneumococco", ignoreCase = true) ||
                u.name.contains("Meningococcico", ignoreCase = true) ||
                u.name.contains("Haemophilus", ignoreCase = true)
            ) {
                u = u.copy(priority = VaccinePriority.ESSENTIAL, reason = "[ASPLENIA — ESSENZIALE] ${u.reason}")
            }
        }

        if (names.any { it.contains("RENALE") || it.contains("KIDNEY") }) {
            if (u.name.contains("Epatite B", ignoreCase = true) && u.status == VaccineStatus.RECOMMENDED) {
                u = u.copy(priority = VaccinePriority.ESSENTIAL, reason = "${u.reason}\n\n[IRC] Utilizzare alto dosaggio.")
            }
        }

        if (names.any { it.contains("EPATICA") || it.contains("LIVER") || it.contains("CIRROSI") }) {
            if (u.name.contains("Epatite A", ignoreCase = true) || u.name.contains("Epatite B", ignoreCase = true)) {
                u = u.copy(priority = VaccinePriority.ESSENTIAL, reason = "[EPATOPATIA — ESSENZIALE] ${u.reason}")
            }
        }

        if (names.any { it.contains("GRAVIDANZA") || it.contains("PREGNANCY") }) {
            if (u.name.contains("Tdap", ignoreCase = true)) {
                u = u.copy(priority = VaccinePriority.ESSENTIAL, reason = "${u.reason}\n\n[GRAVIDANZA] Raccomandata.")
            }
            if (u.type == VaccineType.LIVE) {
                u = u.copy(status = VaccineStatus.CONTRAINDICATED, reason = "${u.reason}\n\n[GRAVIDANZA] Controindicato.")
            }
        }

        // Fallback per condizioni nel DB ma non codificate esplicitamente qui sopra
        if (u.status != VaccineStatus.CONTRAINDICATED) {
            // Se nel DB è controindicato, lo applichiamo solo ai VIVI (LIVE) per cautela generale,
            // a meno che non sia già stato marcato controindicato da regole sopra.
            if (conditions.any { it.raccomandazione == "CONTROINDICATO" } && u.type == VaccineType.LIVE) {
                u = u.copy(status = VaccineStatus.CONTRAINDICATED, reason = "${u.reason}\n\n[CONDIZIONE DB] Vaccino vivo controindicato per stato clinico.")
            }
            // Se nel DB è VALUTARE, lo applichiamo solo se NON è ESSENZIALE (per non oscurare i consigli del biologico o regole specifiche)
            else if (u.status == VaccineStatus.RECOMMENDED && u.priority != VaccinePriority.ESSENTIAL && conditions.any { it.raccomandazione == "VALUTARE" }) {
                u = u.copy(status = VaccineStatus.CAUTION, reason = "${u.reason}\n\n[CONDIZIONE DB] Da valutare con lo specialista.")
            }
        }

        return u
    }
}
