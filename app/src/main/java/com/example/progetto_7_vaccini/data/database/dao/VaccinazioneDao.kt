package com.example.progetto_7_vaccini.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.progetto_7_vaccini.data.database.entities.Vaccinazione

@Dao
interface VaccinazioneDao {

    @Insert
    suspend fun inserisciVaccinazione(vaccinazione: Vaccinazione)

    @Query("SELECT * FROM vaccinazione")
    suspend fun getTutteVaccinazioni(): List<Vaccinazione>

    @Query("SELECT * FROM vaccinazione WHERE idVaccinazione = :id")
    suspend fun getVaccinazioneById(id: Long): Vaccinazione?

    @Query("SELECT * FROM vaccinazione WHERE idPaziente = :idPaziente")
    suspend fun getVaccinazioniByPaziente(idPaziente: Long): List<Vaccinazione>

}