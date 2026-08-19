package com.example.progetto_7_vaccini.data.database

import com.example.progetto_7_vaccini.data.database.entities.CuraBiologica
import com.example.progetto_7_vaccini.data.database.entities.Medico
import com.example.progetto_7_vaccini.data.database.entities.Paziente
import com.example.progetto_7_vaccini.data.database.entities.Utente
import com.example.progetto_7_vaccini.data.database.entities.BiologicType
import com.example.progetto_7_vaccini.data.database.entities.Vaccino
import com.example.progetto_7_vaccini.data.database.entities.CondizioneClinica
import com.example.progetto_7_vaccini.data.database.entities.RaccomandazioneVaccino
import com.example.progetto_7_vaccini.data.database.dao.VaccinazioneDao
import com.example.progetto_7_vaccini.data.database.entities.EsitoVaccino

import com.example.progetto_7_vaccini.data.*
import com.example.progetto_7_vaccini.data.BiologicType as BiologicTypeEnum
import com.example.progetto_7_vaccini.data.database.entities.*

class MotoreDecisionale {

    suspend fun calcolaRaccomandazioniPerPaziente(
        database: AppDatabase,
        idPaziente: Long
    ): List<VaccineRec> {
        val paziente = database.pazienteDao().getPaziente(idPaziente) ?: return emptyList()
        
        // 1. Recupero Cura Biologica
        val curaDb = database.curaBiologicaDao().getCura(paziente.idCura)
        val biologic = BiologicTypeEnum.entries.find { it.label == curaDb?.nome } ?: BiologicTypeEnum.TNF_INHIBITOR
        
        // 2. Recupero Condizioni Cliniche
        val condPaziente = database.pazienteCondizioneDao().getCondizioniByPaziente(idPaziente)
        val tutteCondDb = database.condizioneClinicaDao().getTutteLeCondizioni()
        val conditions = condPaziente.mapNotNull { cp ->
            val nomeCond = tutteCondDb.find { it.idCondizione == cp.idCondizione }?.nome
            MedicalCondition.entries.find { it.label == nomeCond }
        }.toSet()
        
        // 3. Recupero Storia Vaccinale
        val vaccPaziente = database.vaccinazioneDao().getVaccinazioniByPaziente(idPaziente)
        val tuttiVaccDb = database.vaccinoDao().getTuttiVaccini()
        val history = vaccPaziente.mapNotNull { vp ->
            tuttiVaccDb.find { it.idVaccino == vp.idVaccino }?.nome
        }.toSet()
        
        val age = DateUtils.calculateAge(paziente.dataNascita)
        val sex = paziente.sesso.toSex()
        
        // 4. Calcolo tramite le regole esperte
        val raccomandazioni = getVaccineRecommendations(
            sex = sex,
            biologic = biologic,
            age = age,
            conditions = conditions,
            completedVaccines = history
        )
        
        // 5. Salvataggio Esiti nel DB (opzionale, ma utile per persistenza)
        val raccomandazioneDao = database.raccomandazioneVaccinoDao()
        raccomandazioni.forEach { rec ->
            val idVaccino = tuttiVaccDb.find { it.nome == rec.name }?.idVaccino ?: 0L
            if (idVaccino != 0L) {
                raccomandazioneDao.inserisciRaccomandazione(
                    RaccomandazioneVaccino(
                        idPaziente = idPaziente,
                        idVaccino = idVaccino,
                        esito = when(rec.status) {
                            VaccineStatus.RECOMMENDED -> EsitoVaccino.CONSENTITO
                            VaccineStatus.CONTRAINDICATED -> EsitoVaccino.CONTROINDICATO
                            VaccineStatus.CAUTION -> EsitoVaccino.VALUTARE
                            VaccineStatus.ALREADY_DONE -> EsitoVaccino.CONSENTITO // O aggiungere ALREADY_DONE a EsitoVaccino
                        }
                    )
                )
            }
        }
        
        return raccomandazioni
    }
}




