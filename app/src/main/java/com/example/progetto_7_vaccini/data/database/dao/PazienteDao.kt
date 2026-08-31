package com.example.progetto_7_vaccini.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.progetto_7_vaccini.data.database.entities.Paziente

@Dao
interface PazienteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserisciPaziente(paziente: Paziente): Long

    @Query("SELECT * FROM paziente WHERE idPaziente = :id")
    suspend fun getPaziente(id: Long): Paziente?

    @Query("SELECT * FROM paziente WHERE idUtente = :idUtente")
    suspend fun getPazienteByUtente(idUtente: Long): Paziente?

    @Query("SELECT * FROM paziente WHERE idMedico = :idMedico")
    suspend fun getPazientiByMedico(idMedico: Long): List<Paziente>
}