package com.example.progetto_7_vaccini.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.progetto_7_vaccini.data.database.dao.CondizioneClinicaDao
import com.example.progetto_7_vaccini.data.database.dao.CuraBiologicaDao
import com.example.progetto_7_vaccini.data.database.dao.MedicoDao
import com.example.progetto_7_vaccini.data.database.dao.PazienteCondizioneDao
import com.example.progetto_7_vaccini.data.database.dao.PazienteDao
import com.example.progetto_7_vaccini.data.database.dao.RaccomandazioneVaccinoDao
import com.example.progetto_7_vaccini.data.database.dao.UtenteDao
import com.example.progetto_7_vaccini.data.database.dao.VaccinazioneDao
import com.example.progetto_7_vaccini.data.database.dao.VaccinoDao
import com.example.progetto_7_vaccini.data.database.entities.CondizioneClinica
import com.example.progetto_7_vaccini.data.database.entities.CuraBiologica
import com.example.progetto_7_vaccini.data.database.entities.Medico
import com.example.progetto_7_vaccini.data.database.entities.Paziente
import com.example.progetto_7_vaccini.data.database.entities.PazienteCondizione
import com.example.progetto_7_vaccini.data.database.entities.RaccomandazioneVaccino
import com.example.progetto_7_vaccini.data.database.entities.Utente
import com.example.progetto_7_vaccini.data.database.entities.Vaccinazione
import com.example.progetto_7_vaccini.data.database.entities.Vaccino

@Database(
    entities = [
        Utente::class,
        Medico::class,
        Paziente::class,
        CuraBiologica::class,
        CondizioneClinica::class,
        Vaccino::class,
        Vaccinazione::class,
        RaccomandazioneVaccino::class,
        PazienteCondizione::class
    ],
    version = 2
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun utenteDao(): UtenteDao

    abstract fun medicoDao(): MedicoDao

    abstract fun pazienteDao(): PazienteDao

    abstract fun curaBiologicaDao(): CuraBiologicaDao

    abstract fun condizioneClinicaDao(): CondizioneClinicaDao

    abstract fun vaccinoDao(): VaccinoDao

    abstract fun vaccinazioneDao(): VaccinazioneDao

    abstract fun raccomandazioneVaccinoDao(): RaccomandazioneVaccinoDao

    abstract fun pazienteCondizioneDao(): PazienteCondizioneDao
}