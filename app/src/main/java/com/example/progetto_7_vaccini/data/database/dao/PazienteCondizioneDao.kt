package com.example.progetto_7_vaccini.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.progetto_7_vaccini.data.database.entities.PazienteCondizione

@Dao
interface PazienteCondizioneDao {

    @Insert
    suspend fun inserisciPazienteCondizione(pazienteCondizione: PazienteCondizione)

    @Query("SELECT * FROM paziente_condizione")
    suspend fun getTuttePazientiCondizioni(): List<PazienteCondizione>

    @Query("SELECT * FROM paziente_condizione WHERE idPaziente = :idPaziente")
    suspend fun getCondizioniByPaziente(idPaziente: Long): List<PazienteCondizione>

    @Query("SELECT * FROM paziente_condizione WHERE idCondizione = :idCondizione")
    suspend fun getPazientiByCondizione(idCondizione: Long): List<PazienteCondizione>

    @Query("""
    DELETE FROM paziente_condizione
    WHERE idPaziente = :idPaziente
    AND idCondizione = :idCondizione
""")
    suspend fun rimuoviCondizione(
        idPaziente: Long,
        idCondizione: Long
    )

}