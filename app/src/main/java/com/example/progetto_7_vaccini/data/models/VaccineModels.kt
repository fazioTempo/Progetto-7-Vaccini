package com.example.progetto_7_vaccini.data.models

import java.io.Serializable

enum class VaccineStatus { RECOMMENDED, CONTRAINDICATED, CAUTION, ALREADY_DONE }
enum class VaccinePriority { ESSENTIAL, HIGH, ROUTINE }
enum class VaccineType { LIVE, INACTIVATED, RECOMBINANT, SUBUNIT, MRNA, TOXOID }

data class VaccineRec(
    val name: String,
    val brand: String? = null,
    val type: VaccineType,
    val status: VaccineStatus,
    val reason: String,
    val timing: String? = null,
    val priority: VaccinePriority = VaccinePriority.ROUTINE
) : Serializable
