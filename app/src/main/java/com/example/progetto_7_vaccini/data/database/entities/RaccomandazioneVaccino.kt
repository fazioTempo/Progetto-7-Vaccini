package com.example.progetto_7_vaccini.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "raccomandazione_vaccino",
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
data class RaccomandazioneVaccino(

    @PrimaryKey(autoGenerate = true)
    val idRaccomandazione: Long = 0,

    val idPaziente: Long,

    val idVaccino: Long,

    val esito: EsitoVaccino
)