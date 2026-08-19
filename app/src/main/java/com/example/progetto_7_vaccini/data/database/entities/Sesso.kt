package com.example.progetto_7_vaccini.data.database.entities

import com.example.progetto_7_vaccini.data.Sex

enum class Sesso {
    MASCHIO,
    FEMMINA
}

fun Sex.toSesso(): Sesso = when (this) {
    Sex.MALE -> Sesso.MASCHIO
    Sex.FEMALE -> Sesso.FEMMINA
}

fun Sesso.toSex(): Sex = when (this) {
    Sesso.MASCHIO -> Sex.MALE
    Sesso.FEMMINA -> Sex.FEMALE
}
