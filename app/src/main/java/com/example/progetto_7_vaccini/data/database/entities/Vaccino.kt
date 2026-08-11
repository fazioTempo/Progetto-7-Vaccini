package com.example.progetto_7_vaccini.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vaccino")
data class Vaccino(

    @PrimaryKey(autoGenerate = true)
    val idVaccino: Long = 0,

    val nome: String,

    val tipo: String,

    val vivoAttenuato: Boolean
)