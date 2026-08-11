package com.example.progetto_7_vaccini.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "paziente_condizione",
    primaryKeys = ["idPaziente", "idCondizione"],
    foreignKeys = [
        ForeignKey(
            entity = Paziente::class,
            parentColumns = ["idPaziente"],
            childColumns = ["idPaziente"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CondizioneClinica::class,
            parentColumns = ["idCondizione"],
            childColumns = ["idCondizione"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PazienteCondizione(

    val idPaziente: Long,

    val idCondizione: Long
)