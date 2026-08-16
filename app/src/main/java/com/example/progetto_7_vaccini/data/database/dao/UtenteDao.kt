package com.example.progetto_7_vaccini.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.progetto_7_vaccini.data.database.entities.Utente

@Dao
interface UtenteDao {

    @Insert
    suspend fun inserisciUtente(utente: Utente): Long

    @Query("SELECT * FROM utente WHERE idUtente = :id")
    suspend fun getUtente(id: Long): Utente?

    @Query("SELECT * FROM utente WHERE email = :email")
    suspend fun getUtenteByEmail(email: String): Utente?

    @Query("SELECT COUNT(*) FROM utente WHERE ruolo = 'MEDICO'")
    suspend fun contaMedici(): Int

    @Query("UPDATE utente SET email = :nuovaEmail WHERE idUtente = :idUtente")
    suspend fun aggiornaEmail(
        idUtente: Long,
        nuovaEmail: String
    )

    @Query("UPDATE utente SET password = :nuovaPassword WHERE email = :email")
    suspend fun aggiornaPassword(email: String, nuovaPassword: String)
}