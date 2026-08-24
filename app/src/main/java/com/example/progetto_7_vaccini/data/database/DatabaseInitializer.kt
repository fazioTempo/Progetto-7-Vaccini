package com.example.progetto_7_vaccini.data.database

import com.example.progetto_7_vaccini.data.database.dao.CondizioneClinicaDao
import com.example.progetto_7_vaccini.data.database.entities.*
import com.example.progetto_7_vaccini.data.database.dao.VaccinoDao

object DatabaseInitializer {

    suspend fun inizializza(database: AppDatabase) {
        val utenteDao = database.utenteDao()
        val medicoDao = database.medicoDao()
        val pazienteDao = database.pazienteDao()
        val curaBiologicaDao = database.curaBiologicaDao()
        val vaccinoDao = database.vaccinoDao()
        val condizioneClinicaDao = database.condizioneClinicaDao()

        //===================
        //INSERIMENTO VACCINI
        //===================
        val vacciniPresenti = vaccinoDao.getTuttiVaccini()
        if (vacciniPresenti.isEmpty()) {
            inserisciVaccini(vaccinoDao)
        }

        //===================
        //INSERIMENTO CONDIZIONI CLINICHE
        //===================
        val condizioniPresenti = condizioneClinicaDao.getTutteLeCondizioni()
        if (condizioniPresenti.isEmpty()) {
            val listaCondizioni = listOf(
                "Asplenia / asplenia funzionale" to "CONSENTITO",
                "Malattia renale cronica / insufficienza renale" to "VALUTARE",
                "Diabete mellito" to "VALUTARE",
                "BPCO / malattia polmonare cronica" to "VALUTARE",
                "Epatopatia cronica / cirrosi" to "VALUTARE",
                "Infezione da HIV" to "CONTROINDICATO",
                "Cardiopatia cronica / insufficienza cardiaca" to "VALUTARE",
                "Gravidanza" to "CONTROINDICATO",
                "Neoplasia solida / ematologica" to "CONTROINDICATO",
                "Malattia infiammatoria intestinale (Crohn / RCU)" to "VALUTARE"
            )
            listaCondizioni.forEach { (nome, rec) ->
                condizioneClinicaDao.inserisciCondizione(CondizioneClinica(nome = nome, raccomandazione = rec))
            }
        }

        //=============================
        //INSRIMENTO TERAPIE BIOLOGICHE
        //=============================
        val curePresenti = curaBiologicaDao.getTutteLeCure()
        if (curePresenti.isEmpty()) {
            val listaCure = listOf(
                "Inibitore TNF-α (adalimumab, etanercept, infliximab, certolizumab, golimumab)" to "Anti-TNF",
                "Inibitore IL-6 (tocilizumab, sarilumab)" to "Anti-IL-6",
                "Inibitore IL-17 (secukinumab, ixekizumab)" to "Anti-IL-17",
                "Inibitore IL-12/23 (ustekinumab)" to "Anti-IL-12/23",
                "Inibitore IL-23 (guselkumab, risankizumab)" to "Anti-IL-23",
                "Deplettore di cellule B (rituximab, obinutuzumab)" to "Anti-CD20",
                "Bloccante della co-stimolazione T (abatacept)" to "Anti-CTLA-4",
                "Inibitore JAK (tofacitinib, baricitinib, upadacitinib)" to "JAKi",
                "Inibitore VEGF (bevacizumab, ramucirumab)" to "Anti-VEGF",
                "Inibitore BLyS/BAFF (belimumab)" to "Anti-BLyS",
                "Inibitore dell'integrina selettivo intestinale (vedolizumab, natalizumab)" to "Anti-integrina"
            )
            listaCure.forEach { (nome, principio) ->
                curaBiologicaDao.inserisciCuraBiologica(CuraBiologica(nome = nome, principioAttivo = principio))
            }
        }

        // Se ci sono già medici, non inseriamo i dati di test
        if (utenteDao.contaMedici() > 0) return

        val tutteLeCure = curaBiologicaDao.getTutteLeCure()
        val idCura1 = tutteLeCure.getOrNull(0)?.idCura ?: 1L
        val idCura2 = tutteLeCure.getOrNull(1)?.idCura ?: 2L

        // MEDICI
        val idUtenteMedico1 = utenteDao.inserisciUtente(Utente(email = "medico1@gmail.com", password = "password1", ruolo = "MEDICO"))
        val idMedico1 = medicoDao.inserisciMedico(Medico(idUtente = idUtenteMedico1, nome = "Mario", cognome = "Rossi"))

        val idUtenteMedico2 = utenteDao.inserisciUtente(Utente(email = "medico2@gmail.com", password = "password2", ruolo = "MEDICO"))
        val idMedico2 = medicoDao.inserisciMedico(Medico(idUtente = idUtenteMedico2, nome = "Luigi", cognome = "Bianchi"))

        // PAZIENTI
        val idUtentePaziente1 = utenteDao.inserisciUtente(Utente(email = "paziente1@gmail.com", password = "password1", ruolo = "PAZIENTE"))
        pazienteDao.inserisciPaziente(Paziente(idUtente = idUtentePaziente1, idMedico = idMedico1, idCura = idCura1, nome = "Giovanni", cognome = "Verdi", dataNascita = "15/05/2000", sesso = Sesso.MASCHIO))

        val idUtentePaziente2 = utenteDao.inserisciUtente(Utente(email = "paziente2@gmail.com", password = "password2", ruolo = "PAZIENTE"))
        pazienteDao.inserisciPaziente(Paziente(idUtente = idUtentePaziente2, idMedico = idMedico2, idCura = idCura2, nome = "Anna", cognome = "Neri", dataNascita = "20/08/2001", sesso = Sesso.FEMMINA))
    }

    private suspend fun inserisciVaccini(vaccinoDao: VaccinoDao) {
        val vaccini = listOf(
            Vaccino(1, "Influenza (inattivato)", "INATTIVATO", false),
            Vaccino(2, "Pneumococco PCV20", "CONIUGATO", false),
            Vaccino(3, "Pneumococco PPSV23", "POLISACCARIDICO", false),
            Vaccino(4, "Epatite A", "INATTIVATO", false),
            Vaccino(5, "Epatite B", "INATTIVATO", false),
            Vaccino(6, "HPV", "PROTEICO", false),
            Vaccino(7, "Tetano-Difterite-Pertosse (Tdap)", "TOKSOIDE", false),
            Vaccino(8, "Polio IPV", "INATTIVATO", false),
            Vaccino(9, "Meningococco ACWY", "CONIUGATO", false),
            Vaccino(10, "Meningococco B", "PROTEICO", false),
            Vaccino(11, "COVID-19 mRNA", "MRNA", false),
            Vaccino(12, "Herpes Zoster (Shingrix)", "PROTEICO", false),
            Vaccino(20, "MMR", "VIVO_ATTENUATO", true),
            Vaccino(21, "Varicella", "VIVO_ATTENUATO", true),
            Vaccino(22, "Zoster vivo (Zostavax)", "VIVO_ATTENUATO", true),
            Vaccino(23, "Rotavirus", "VIVO_ATTENUATO", true),
            Vaccino(24, "Febbre gialla", "VIVO_ATTENUATO", true),
            Vaccino(25, "Polio OPV", "VIVO_ATTENUATO", true),
            Vaccino(26, "Tifo orale (Ty21a)", "VIVO_ATTENUATO", true),
            Vaccino(27, "BCG", "VIVO_ATTENUATO", true),
            Vaccino(30, "Vaccini vivi per viaggi internazionali", "VIVO_ATTENUATO", true)
        )
        vaccini.forEach { vaccinoDao.inserisciVaccino(it) }
    }
}
