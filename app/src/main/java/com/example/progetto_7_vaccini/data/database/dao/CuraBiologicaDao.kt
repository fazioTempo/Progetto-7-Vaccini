package com.example.progetto_7_vaccini.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.progetto_7_vaccini.data.database.entities.CuraBiologica

@Dao
interface CuraBiologicaDao {

    @Insert
    suspend fun inserisciCuraBiologica(curaBiologica: CuraBiologica): Long

    @Query("SELECT * FROM cura_biologica WHERE idCura = :id")
    suspend fun getCura(id: Long): CuraBiologica?

    @Query("SELECT * FROM cura_biologica")
    suspend fun getTutteLeCure(): List<CuraBiologica>
}