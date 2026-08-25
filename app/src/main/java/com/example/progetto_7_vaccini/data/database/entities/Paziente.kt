package com.example.progetto_7_vaccini.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "paziente",
    foreignKeys = [
        ForeignKey(
            entity = Utente::class,
            parentColumns = ["idUtente"],
            childColumns = ["idUtente"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Medico::class,
            parentColumns = ["idMedico"],
            childColumns = ["idMedico"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = CuraBiologica::class,
            parentColumns = ["idCura"],
            childColumns = ["idCura"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class Paziente(

    @PrimaryKey(autoGenerate = true)
    val idPaziente: Long = 0,

    val idUtente: Long,

    val idMedico: Long,

    val idCura: Long,

    val nome: String,

    val cognome: String,

    val dataNascita: String,

    val sesso: Sesso
) : Serializable
