package com.example.progetto_7_vaccini.data.repository

import com.example.progetto_7_vaccini.data.database.AppDatabase
import com.example.progetto_7_vaccini.data.database.MotoreDecisionale
import com.example.progetto_7_vaccini.data.database.entities.*
import com.example.progetto_7_vaccini.data.models.*

class VaccineRepository(private val database: AppDatabase) {

    private val motore = MotoreDecisionale()

    suspend fun getOrCalculate(idPaziente: Long): List<VaccineRec> {
        val cached = database.raccomandazioneVaccinoDao().getRaccomandazioniCompleteByPaziente(idPaziente)
        
        return if (cached.isNotEmpty()) {
            cached.map { item ->
                VaccineRec(
                    name = item.vaccino.nome,
                    brand = item.raccomandazione.brand,
                    type = item.vaccino.tipo,
                    status = when(item.raccomandazione.esito) {
                        EsitoVaccino.CONSENTITO -> VaccineStatus.RECOMMENDED
                        EsitoVaccino.CONTROINDICATO -> VaccineStatus.CONTRAINDICATED
                        EsitoVaccino.VALUTARE -> VaccineStatus.CAUTION
                        EsitoVaccino.FATTO -> VaccineStatus.ALREADY_DONE
                    },
                    reason = item.raccomandazione.motivazione,
                    timing = item.raccomandazione.tempistiche,
                    priority = VaccinePriority.valueOf(item.raccomandazione.priorita)
                )
            }
        } else {
            refresh(idPaziente)
        }
    }

    suspend fun refresh(idPaziente: Long): List<VaccineRec> {
        val results = motore.calcolaRaccomandazioniPerPaziente(database, idPaziente)
        
        val dao = database.raccomandazioneVaccinoDao()
        val tuttiVaccini = database.vaccinoDao().getTuttiVaccini()
        
        dao.cancellaRaccomandazioniPerPaziente(idPaziente)
        
        results.forEach { rec ->
            val idVaccino = tuttiVaccini.find { it.nome == rec.name }?.idVaccino ?: 0L
            if (idVaccino != 0L) {
                dao.inserisciRaccomandazione(
                    RaccomandazioneVaccino(
                        idPaziente = idPaziente,
                        idVaccino = idVaccino,
                        esito = when(rec.status) {
                            VaccineStatus.RECOMMENDED -> EsitoVaccino.CONSENTITO
                            VaccineStatus.CONTRAINDICATED -> EsitoVaccino.CONTROINDICATO
                            VaccineStatus.CAUTION -> EsitoVaccino.VALUTARE
                            VaccineStatus.ALREADY_DONE -> EsitoVaccino.FATTO
                        },
                        brand = rec.brand,
                        motivazione = rec.reason,
                        tempistiche = rec.timing,
                        priorita = rec.priority.name
                    )
                )
            }
        }
        
        return results
    }
}
