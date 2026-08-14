package com.example.progetto_7_vaccini.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.progetto_7_vaccini.data.database.entities.Medico

@Dao
interface MedicoDao {

    @Insert
    suspend fun inserisciMedico(medico: Medico): Long

    @Query("SELECT * FROM medico WHERE idMedico = :id")
    suspend fun getMedico(id: Long): Medico?

    @Query("SELECT * FROM medico WHERE idUtente = :idUtente")
    suspend fun getMedicoByUtente(idUtente: Long): Medico?
}