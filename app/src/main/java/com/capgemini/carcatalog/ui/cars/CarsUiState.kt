package com.capgemini.carcatalog.ui.cars

import com.capgemini.carcatalog.domain.CarModel

sealed interface CarsUiState {
    object Loading : CarsUiState
    object Error : CarsUiState
    data class Data(val cars: List<CarUiModel>, val isRefreshing: Boolean) : CarsUiState
}

data class CarUiModel(
    val name: String,
    val description: String,
    val imageUrl: String
)


fun CarModel.toCarUiModel(): CarUiModel {
    return CarUiModel(
        name = name,
        description = description,
        imageUrl = imageUrl
    )
}