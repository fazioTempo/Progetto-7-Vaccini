package com.example.progetto_7_vaccini.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "utente",
    indices = [
        Index(value = ["email"], unique = true)
    ]
)
data class Utente(

    @PrimaryKey(autoGenerate = true)
    val idUtente: Long = 0,

    val email: String,
    val password: String,
    val ruolo: String
)