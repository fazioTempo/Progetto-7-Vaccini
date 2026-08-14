package com.example.progetto_7_vaccini.data.database

import com.example.progetto_7_vaccini.data.database.entities.CuraBiologica
import com.example.progetto_7_vaccini.data.database.entities.Medico
import com.example.progetto_7_vaccini.data.database.entities.Paziente
import com.example.progetto_7_vaccini.data.database.entities.Utente

object DatabaseInitializer {

    suspend fun inizializza(database: AppDatabase) {

        val utenteDao = database.utenteDao()

        if (utenteDao.contaMedici() > 0) {
            return
        }

        val medicoDao = database.medicoDao()
        val pazienteDao = database.pazienteDao()
        val curaBiologicaDao = database.curaBiologicaDao()

        // =========================
        // MEDICO 1
        // =========================

        val idUtenteMedico1 = utenteDao.inserisciUtente(
            Utente(
                email = "medico1@gmail.com",
                password = "password1",
                ruolo = "MEDICO"
            )
        )

        val idMedico1 = medicoDao.inserisciMedico(
            Medico(
                idUtente = idUtenteMedico1,
                nome = "Mario",
                cognome = "Rossi"
            )
        )

        // =========================
        // MEDICO 2
        // =========================

        val idUtenteMedico2 = utenteDao.inserisciUtente(
            Utente(
                email = "medico2@gmail.com",
                password = "password2",
                ruolo = "MEDICO"
            )
        )

        val idMedico2 = medicoDao.inserisciMedico(
            Medico(
                idUtente = idUtenteMedico2,
                nome = "Luigi",
                cognome = "Bianchi"
            )
        )

        // =========================
        // CURE BIOLOGICHE
        // =========================

        val idCura1 = curaBiologicaDao.inserisciCuraBiologica(
            CuraBiologica(
                nome = "Cura biologica 1",
                principioAttivo = "Principio attivo 1"
            )
        )

        val idCura2 = curaBiologicaDao.inserisciCuraBiologica(
            CuraBiologica(
                nome = "Cura biologica 2",
                principioAttivo = "Principio attivo 2"
            )
        )

        // =========================
        // PAZIENTE 1
        // =========================

        val idUtentePaziente1 = utenteDao.inserisciUtente(
            Utente(
                email = "paziente1@gmail.com",
                password = "password1",
                ruolo = "PAZIENTE"
            )
        )

        pazienteDao.inserisciPaziente(
            Paziente(
                idUtente = idUtentePaziente1,
                idMedico = idMedico1,
                idCura = idCura1,
                nome = "Giovanni",
                cognome = "Verdi",
                dataNascita = "15/05/2000",
                sesso = "M"
            )
        )

        // =========================
        // PAZIENTE 2
        // =========================

        val idUtentePaziente2 = utenteDao.inserisciUtente(
            Utente(
                email = "paziente2@gmail.com",
                password = "password2",
                ruolo = "PAZIENTE"
            )
        )

        pazienteDao.inserisciPaziente(
            Paziente(
                idUtente = idUtentePaziente2,
                idMedico = idMedico2,
                idCura = idCura2,
                nome = "Anna",
                cognome = "Neri",
                dataNascita = "20/08/2001",
                sesso = "F"
            )
        )
    }
}