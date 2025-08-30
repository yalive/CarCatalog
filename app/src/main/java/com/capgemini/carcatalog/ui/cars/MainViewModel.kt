package com.capgemini.carcatalog.ui.cars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.capgemini.carcatalog.common.Result
import com.capgemini.carcatalog.data.model.toCarUiModel
import com.capgemini.carcatalog.data.repository.CarCatalogRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val carCatalogRepository: CarCatalogRepository
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
            val result = carCatalogRepository.getCars()
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
        val result = carCatalogRepository.getCars()
        val state = when (result) {
            is Result.Error -> CarsUiState.Error
            is Result.Success -> CarsUiState.Data(
                cars = result.data.map { it.toCarUiModel() },
                isRefreshing = false
            )
        }
        _uiState.value = state
    }

    companion object {

        object CarCatalogRepositoryKey : CreationExtras.Key<CarCatalogRepository>

        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = this[CarCatalogRepositoryKey] as CarCatalogRepository
                MainViewModel(repository)
            }
        }
    }
}