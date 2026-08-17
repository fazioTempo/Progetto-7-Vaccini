package com.example.progetto_7_vaccini.data.database

import com.example.progetto_7_vaccini.data.database.entities.CuraBiologica
import com.example.progetto_7_vaccini.data.database.entities.Medico
import com.example.progetto_7_vaccini.data.database.entities.Paziente
import com.example.progetto_7_vaccini.data.database.entities.Utente
import com.example.progetto_7_vaccini.data.database.entities.BiologicType
import com.example.progetto_7_vaccini.data.database.entities.Vaccino
import com.example.progetto_7_vaccini.data.database.entities.CondizioneClinica

class MotoreDecisionale {

    suspend fun valutaVaccinoPerPaziente(
        vaccino: Vaccino,
        biologico: CuraBiologica,
        condizioni: List<CondizioneClinica>
    ): String {

        // VALUTAZIONE VACCINO (VIVO ATTENUATO = true)
        if (vaccino.vivoAttenuato) {
            return "CONTROINDICATO"
        }

        // VALUTAZIONE CONDIZIONE
        val esitoCondizioni = valutaCondizioniCliniche(vaccino, condizioni)
        if (esitoCondizioni == "CONTROINDICATO") {
            return "CONTROINDICATO"
        }

        // 3) Se non è vivo attenuato → sempre consentito
        if (!vaccino.vivoAttenuato) {
            return "CONSENTITO"
        }

        // 4) Se è vivo attenuato ma non ci sono controindicazioni → VALUTARE
        return "VALUTARE"
    }

    fun valutaCondizioniCliniche(
        vaccino: Vaccino,
        condizioni: List<CondizioneClinica>
    ): String {

        // 1) Se vaccino è vivo attenuato
        if (vaccino.vivoAttenuato) {

            // 2) Se il paziente ha condizioni che vietano vaccini vivi
            val controindicazioni = listOf("GRAVIDANZA", "IMMUNOSOPPRESSIONE", "HIV_CD4_BASSO", "TRAPIANTO")

            if (condizioni.any { it.nome in controindicazioni }) {
                return "CONTROINDICATO"
            }

            // 3) Altre condizioni → valutare
            return "VALUTARE"
        }

        // 4) Vaccini non vivi → sempre consentiti
        return "CONSENTITO"
    }

    fun valutaBiologico(
        vaccino: Vaccino,
        biologico: CuraBiologica
    ): String {

        if (vaccino.vivoAttenuato ) {
            return "CONTROINDICATO"
        }

        return "CONSENTITO"
    }


}