package com.example.progetto_7_vaccini.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "condizione_clinica")
data class CondizioneClinica(

    @PrimaryKey(autoGenerate = true)
    val idCondizione: Long = 0,

    val nome: String,
    val raccomandazione: String
)