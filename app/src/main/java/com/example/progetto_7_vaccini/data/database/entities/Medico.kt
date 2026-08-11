package com.example.progetto_7_vaccini.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "medico",
    foreignKeys = [
        ForeignKey(
            entity = Utente::class,
            parentColumns = ["idUtente"],
            childColumns = ["idUtente"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Medico(

    @PrimaryKey(autoGenerate = true)
    val idMedico: Long = 0,

    val idUtente: Long,

    val nome: String,
    val cognome: String
)