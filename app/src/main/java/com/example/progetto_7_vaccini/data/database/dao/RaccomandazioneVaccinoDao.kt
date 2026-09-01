package com.example.progetto_7_vaccini.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Embedded
import androidx.room.Relation
import com.example.progetto_7_vaccini.data.database.entities.RaccomandazioneVaccino
import com.example.progetto_7_vaccini.data.database.entities.Vaccino

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

    @Query("DELETE FROM raccomandazione_vaccino WHERE idPaziente = :idPaziente")
    suspend fun cancellaRaccomandazioniPerPaziente(idPaziente: Long)

    @Transaction
    @Query("SELECT * FROM raccomandazione_vaccino WHERE idPaziente = :idPaziente")
    suspend fun getRaccomandazioniCompleteByPaziente(idPaziente: Long): List<RaccomandazioneConDettagli>
}

data class RaccomandazioneConDettagli(
    @Embedded val raccomandazione: RaccomandazioneVaccino,
    @Relation(
        parentColumn = "idVaccino",
        entityColumn = "idVaccino"
    )
    val vaccino: Vaccino
)

