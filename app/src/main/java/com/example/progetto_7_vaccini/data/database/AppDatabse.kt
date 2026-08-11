package com.example.progetto_7_vaccini.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.progetto_7_vaccini.data.database.entities.*
import androidx.room.TypeConverters
@TypeConverters(Converters::class)

@Database(
    entities = [
        Utente::class,
        Medico::class,
        Paziente::class,
        CuraBiologica::class,
        CondizioneClinica::class,
        PazienteCondizione::class,
        Vaccino::class,
        Vaccinazione::class,
        RaccomandazioneVaccino::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase()

