package com.example.progetto_7_vaccini.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.progetto_7_vaccini.data.database.entities.Paziente

@Dao
interface PazienteDao {

    @Insert
    suspend fun inserisciPaziente(paziente: Paziente)

    @Query("SELECT * FROM paziente WHERE idPaziente = :id")
    suspend fun getPaziente(id: Long): Paziente?

    @Query("SELECT * FROM paziente WHERE idUtente = :idUtente")
    suspend fun getPazienteByUtente(idUtente: Long): Paziente?

    @Query("SELECT * FROM paziente WHERE idMedico = :idMedico")
    suspend fun getPazientiByMedico(idMedico: Long): List<Paziente>

    @Query("UPDATE paziente SET idCura = :idCura WHERE idPaziente = :idPaziente")
    suspend fun aggiornaCura(
        idPaziente: Long,
        idCura: Long
    )
}