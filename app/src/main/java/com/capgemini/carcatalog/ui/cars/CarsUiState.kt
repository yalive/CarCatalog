package com.capgemini.carcatalog.ui.cars

sealed interface CarsUiState {
    object Loading : CarsUiState
    object Error : CarsUiState
    data class Data(val cars: List<CarUiModel>, val isRefreshing: Boolean) : CarsUiState
}

data class CarUiModel(
    val name: String,
    val description: String
)