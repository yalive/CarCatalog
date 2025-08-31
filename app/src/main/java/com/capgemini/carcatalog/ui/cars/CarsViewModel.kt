package com.capgemini.carcatalog.ui.cars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capgemini.carcatalog.common.Result
import com.capgemini.carcatalog.domain.GetCarsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarsViewModel @Inject constructor(
    private val getCars: GetCarsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CarsUiState>(CarsUiState.Loading)
    val uiState: StateFlow<CarsUiState> = _uiState

    init {
        prepareCarCatalog()
    }

    fun refresh() {
        val uiData = _uiState.value as? CarsUiState.Data ?: return
        if (uiData.isRefreshing) return
        _uiState.value = uiData.copy(isRefreshing = true)
        viewModelScope.launch {
            val result = getCars()
            val state = when (result) {
                is Result.Error -> uiData
                is Result.Success -> CarsUiState.Data(
                    cars = result.data.map { it.toCarUiModel() },
                    isRefreshing = false
                )
            }
            _uiState.value = state
        }
    }

    fun retry() {
        prepareCarCatalog()
    }

    private fun prepareCarCatalog() = viewModelScope.launch {
        _uiState.value = CarsUiState.Loading
        val result = getCars()
        val state = when (result) {
            is Result.Error -> CarsUiState.Error
            is Result.Success -> CarsUiState.Data(
                cars = result.data.map { it.toCarUiModel() },
                isRefreshing = false
            )
        }
        _uiState.value = state
    }
}