package com.example.progetto_7_vaccini.data.database

import androidx.room.TypeConverter
import com.example.progetto_7_vaccini.data.database.entities.EsitoVaccino
import com.example.progetto_7_vaccini.data.database.entities.Sesso

class Converters {

    @TypeConverter
    fun fromEsitoVaccino(esito: EsitoVaccino): String {
        return esito.name
    }

    @TypeConverter
    fun toEsitoVaccino(esito: String): EsitoVaccino {
        return EsitoVaccino.valueOf(esito)
    }

    @TypeConverter
    fun fromSesso(sesso: Sesso): String {
        return sesso.name
    }

    @TypeConverter
    fun toSesso(sesso: String): Sesso {
        return Sesso.valueOf(sesso)
    }
}