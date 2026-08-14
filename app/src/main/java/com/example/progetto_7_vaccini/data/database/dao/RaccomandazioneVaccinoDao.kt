package com.example.progetto_7_vaccini.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.progetto_7_vaccini.data.database.entities.RaccomandazioneVaccino

@Dao
interface RaccomandazioneVaccinoDao {

    @Insert
    suspend fun inserisciRaccomandazione(raccomandazione: RaccomandazioneVaccino)

    @Query("SELECT * FROM raccomandazione_vaccino")
    suspend fun getTutteRaccomandazioni(): List<RaccomandazioneVaccino>

    @Query("SELECT * FROM raccomandazione_vaccino WHERE idRaccomandazione = :id")
    suspend fun getRaccomandazioneById(id: Long): RaccomandazioneVaccino?

    @Query("SELECT * FROM raccomandazione_vaccino WHERE idPaziente = :idPaziente")
    suspend fun getRaccomandazioniByPaziente(idPaziente: Long): List<RaccomandazioneVaccino>

    @Query("SELECT * FROM raccomandazione_vaccino WHERE idVaccino = :idVaccino")
    suspend fun getRaccomandazioniByVaccino(idVaccino: Long): List<RaccomandazioneVaccino>
}