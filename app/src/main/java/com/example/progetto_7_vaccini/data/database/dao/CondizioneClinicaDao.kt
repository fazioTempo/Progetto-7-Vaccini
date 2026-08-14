package com.example.progetto_7_vaccini.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.progetto_7_vaccini.data.database.entities.CondizioneClinica

@Dao
interface CondizioneClinicaDao {

    @Insert
    suspend fun inserisciCondizione(condizione: CondizioneClinica)

    @Query("SELECT * FROM condizione_clinica WHERE idCondizione = :id")
    suspend fun getCondizione(id: Long): CondizioneClinica?

    @Query("SELECT * FROM condizione_clinica")
    suspend fun getTutteLeCondizioni(): List<CondizioneClinica>
}