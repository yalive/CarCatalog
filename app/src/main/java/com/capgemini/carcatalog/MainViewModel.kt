package com.capgemini.carcatalog

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val _cars = MutableStateFlow<List<CarUiModel>>(emptyList())
    val cars: StateFlow<List<CarUiModel>> = _cars

    init {
        prepareCarCatalog()
    }

    private fun prepareCarCatalog() {
        val carList = buildList {
            for (index in 1..20) {
                add(
                    CarUiModel(
                        name = "Car $index",
                        description = "Car description $index"
                    )
                )
            }
        }
        _cars.value = carList
    }
}

data class CarUiModel(
    val name: String,
    val description: String
)