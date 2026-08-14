package com.example.progetto_7_vaccini.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.progetto_7_vaccini.data.database.entities.Vaccino

@Dao
interface VaccinoDao {

    @Insert
    suspend fun inserisciVaccino(vaccino: Vaccino)

    @Query("SELECT * FROM vaccino")
    suspend fun getTuttiVaccini(): List<Vaccino>

    @Query("SELECT * FROM vaccino WHERE idVaccino = :id")
    suspend fun getVaccinoById(id: Long): Vaccino?

}