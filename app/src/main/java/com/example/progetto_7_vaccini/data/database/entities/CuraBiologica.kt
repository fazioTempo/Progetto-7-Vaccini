package com.example.progetto_7_vaccini.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cura_biologica")
data class CuraBiologica(

    @PrimaryKey(autoGenerate = true)
    val idCura: Long = 0,

    val nome: String,
    val principioAttivo: String
)