package com.capgemini.carcatalog.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.capgemini.carcatalog.data.CarCatalogRepository
import com.capgemini.carcatalog.data.toCarUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val carCatalogRepository: CarCatalogRepository
) : ViewModel() {

    private val _cars = MutableStateFlow<List<CarUiModel>>(emptyList())
    val cars: StateFlow<List<CarUiModel>> = _cars

    init {
        prepareCarCatalog()
    }

    private fun prepareCarCatalog() = viewModelScope.launch {
        try {
            val carList = carCatalogRepository.getCars().map { it.toCarUiModel() }
            _cars.value = carList
        } catch (e: Exception) {
            print(e)
        }
    }

    companion object {

        object CarCatalogRepositoryKey : CreationExtras.Key<CarCatalogRepository>

        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val repository = this[CarCatalogRepositoryKey] as CarCatalogRepository
                MainViewModel(repository)
            }
        }

        // Not used
        @Suppress("UNCHECKED_CAST")
        fun factory(
            carCatalogRepository: CarCatalogRepository
        ) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                return MainViewModel(carCatalogRepository) as T
            }
        }
    }
}

data class CarUiModel(
    val name: String,
    val description: String
)