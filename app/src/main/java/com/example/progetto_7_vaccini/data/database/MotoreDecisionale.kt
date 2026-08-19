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

class MotoreDecisionale {

    suspend fun calcolaRaccomandazioniPerPaziente(
        database: AppDatabase,
        idPaziente: Long,
        biologico: CuraBiologica,
        condizioni: List<CondizioneClinica>
    ) {
        val vaccinoDao = database.vaccinoDao()
        val raccomandazioneDao = database.raccomandazioneVaccinoDao()

        val tuttiVaccini = vaccinoDao.getTuttiVaccini()

        tuttiVaccini.forEach { vaccino ->

            val esito = valutaVaccinoPerPaziente(
                vaccino = vaccino,
                condizioni = condizioni
            )

            val raccomandazione = RaccomandazioneVaccino(
                idPaziente = idPaziente,
                idVaccino = vaccino.idVaccino,
                esito = esito
            )

            raccomandazioneDao.inserisciRaccomandazione(raccomandazione)
        }
    }

        fun valutaVaccinoPerPaziente(
            vaccino: Vaccino,
            condizioni: List<CondizioneClinica>
        ): EsitoVaccino {

            // 1) Vaccini vivi attenuati → controindicati
            if (vaccino.vivoAttenuato) {
                return EsitoVaccino.CONTROINDICATO
            }

            // 2) Condizioni cliniche
            val esitoCondizioni = valutaCondizioniCliniche(condizioni)
            if (esitoCondizioni == "CONTROINDICATO") {
                return EsitoVaccino.CONTROINDICATO
            }

            if (esitoCondizioni == "VALUTARE") {
                return EsitoVaccino.VALUTARE
            }

            // 3) Vaccini non vivi → sempre consentiti
            return EsitoVaccino.CONSENTITO
        }

        fun valutaCondizioniCliniche(
            condizioni: List<CondizioneClinica>
        ): String {

            // Condizioni che vietano vaccini vivi attenuati
            if (condizioni.any { it.raccomandazione == "CONTROINDICATO" }) {
                return "CONTROINDICATO"
            }

            // Condizioni che richiedono cautela
            if (condizioni.any { it.raccomandazione == "VALUTARE" }) {
                return "VALUTARE"
            }

            return "CONSENTITO"
        }
    }




