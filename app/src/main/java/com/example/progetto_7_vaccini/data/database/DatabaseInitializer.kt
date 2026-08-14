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
        // CURE BIOLOGICHE (Sincronizzate con BiologicType Enum)
        // =========================
        
        com.example.progetto_7_vaccini.data.BiologicType.entries.forEach { type ->
            curaBiologicaDao.inserisciCuraBiologica(
                CuraBiologica(
                    nome = type.label,
                    principioAttivo = type.shortName
                )
            )
        }

        val tutteLeCure = curaBiologicaDao.getTutteLeCure()
        val idCura1 = tutteLeCure.getOrNull(0)?.idCura ?: 1L
        val idCura2 = tutteLeCure.getOrNull(1)?.idCura ?: 2L

        // =========================
        // MEDICI
        // =========================

        val idUtenteMedico1 = utenteDao.inserisciUtente(
            Utente(email = "medico1@gmail.com", password = "password1", ruolo = "MEDICO")
        )
        val idMedico1 = medicoDao.inserisciMedico(
            Medico(idUtente = idUtenteMedico1, nome = "Mario", cognome = "Rossi")
        )

        val idUtenteMedico2 = utenteDao.inserisciUtente(
            Utente(email = "medico2@gmail.com", password = "password2", ruolo = "MEDICO")
        )
        val idMedico2 = medicoDao.inserisciMedico(
            Medico(idUtente = idUtenteMedico2, nome = "Luigi", cognome = "Bianchi")
        )

        // =========================
        // PAZIENTI DI ESEMPIO
        // =========================

        val idUtentePaziente1 = utenteDao.inserisciUtente(
            Utente(email = "paziente1@gmail.com", password = "password1", ruolo = "PAZIENTE")
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

        val idUtentePaziente2 = utenteDao.inserisciUtente(
            Utente(email = "paziente2@gmail.com", password = "password2", ruolo = "PAZIENTE")
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
