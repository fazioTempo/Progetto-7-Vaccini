package com.example.progetto_7_vaccini.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.progetto_7_vaccini.data.database.AppDatabase
import com.example.progetto_7_vaccini.data.database.MotoreDecisionale
import com.example.progetto_7_vaccini.data.database.entities.CondizioneClinica
import com.example.progetto_7_vaccini.data.database.entities.CuraBiologica
import com.example.progetto_7_vaccini.data.database.entities.Vaccino
import com.example.progetto_7_vaccini.data.models.VaccineRec
import com.example.progetto_7_vaccini.data.repository.VaccineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(database: AppDatabase) : ViewModel() {

    private val repository = VaccineRepository(database)
    private val motore = MotoreDecisionale()

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _recommendations = MutableStateFlow<List<VaccineRec>>(emptyList())
    val recommendations: StateFlow<List<VaccineRec>> = _recommendations.asStateFlow()

    fun loadRecommendations(idPaziente: Long, forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val results = if (forceRefresh) {
                    repository.refresh(idPaziente)
                } else {
                    repository.getOrCalculate(idPaziente)
                }
                _recommendations.value = results
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Errore sconosciuto")
            }
        }
    }

    fun calculateGuest(
        sex: String,
        biologic: CuraBiologica,
        age: Int?,
        conditions: List<CondizioneClinica>,
        history: Set<Long>,
        vaccineOptions: List<Vaccino>
    ) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val results = motore.calcolaVolatile(
                    sexLabel = sex,
                    biologicName = biologic.nome,
                    age = age,
                    selectedConditions = conditions,
                    completedVaccineIds = history,
                    tuttiVacciniDb = vaccineOptions
                )
                _recommendations.value = results
                _uiState.value = UiState.Success
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Errore sconosciuto")
            }
        }
    }

    fun clearRecommendations() {
        _recommendations.value = emptyList()
        _uiState.value = UiState.Idle
    }

    sealed class UiState {
        object Idle : UiState()
        object Loading : UiState()
        object Success : UiState()
        data class Error(val message: String) : UiState()
    }
}
