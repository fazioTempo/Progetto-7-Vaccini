package com.example.progetto_7_vaccini.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.progetto_7_vaccini.data.database.entities.EsitoVaccino
import com.example.progetto_7_vaccini.data.database.entities.RaccomandazioneVaccino

@Dao
interface RaccomandazioneVaccinoDao {

    @Insert
    suspend fun inserisciRaccomandazione(
        raccomandazione: RaccomandazioneVaccino
    )

    @Query("SELECT * FROM raccomandazione_vaccino WHERE idRaccomandazione = :id")
    suspend fun getRaccomandazione(
        id: Long
    ): RaccomandazioneVaccino?

    @Query("""
        SELECT * FROM raccomandazione_vaccino
        WHERE idPaziente = :idPaziente
    """)
    suspend fun getRaccomandazioniByPaziente(
        idPaziente: Long
    ): List<RaccomandazioneVaccino>

    @Query("""
        SELECT * FROM raccomandazione_vaccino
        WHERE idVaccino = :idVaccino
    """)
    suspend fun getRaccomandazioniByVaccino(
        idVaccino: Long
    ): List<RaccomandazioneVaccino>

    @Query("""
        UPDATE raccomandazione_vaccino
        SET esito = :esito
        WHERE idRaccomandazione = :idRaccomandazione
    """)
    suspend fun aggiornaEsito(
        idRaccomandazione: Long,
        esito: EsitoVaccino
    )
}