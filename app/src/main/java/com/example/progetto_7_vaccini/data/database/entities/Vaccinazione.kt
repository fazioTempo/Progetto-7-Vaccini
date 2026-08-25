package com.example.progetto_7_vaccini.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "vaccinazione",
    foreignKeys = [
        ForeignKey(
            entity = Paziente::class,
            parentColumns = ["idPaziente"],
            childColumns = ["idPaziente"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Vaccino::class,
            parentColumns = ["idVaccino"],
            childColumns = ["idVaccino"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class Vaccinazione(

    @PrimaryKey(autoGenerate = true)
    val idVaccinazione: Long = 0,

    val idPaziente: Long,

    val idVaccino: Long,

    val dataSomministrazione: String,

    val numeroDose: Int
)