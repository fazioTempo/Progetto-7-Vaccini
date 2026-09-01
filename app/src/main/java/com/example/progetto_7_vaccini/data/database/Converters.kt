package com.example.progetto_7_vaccini.data.database

import androidx.room.TypeConverter
import com.example.progetto_7_vaccini.data.database.entities.EsitoVaccino
import com.example.progetto_7_vaccini.data.database.entities.Sesso
import com.example.progetto_7_vaccini.data.models.VaccineType

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

    @TypeConverter
    fun fromVaccineType(tipo: VaccineType): String {
        return tipo.name
    }

    @TypeConverter
    fun toVaccineType(tipo: String): VaccineType {
        return VaccineType.valueOf(tipo)
    }
}