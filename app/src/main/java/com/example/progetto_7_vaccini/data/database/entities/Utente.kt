package com.example.progetto_7_vaccini.data.database.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "utente")
data class Utente(
    @PrimaryKey(autoGenerate = true)
    val idUtente: Long = 0,

    val email: String,
    val password: String,
    val ruolo: String
)