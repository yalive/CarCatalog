package com.capgemini.carcatalog.ui.cars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capgemini.carcatalog.common.Result
import com.capgemini.carcatalog.domain.GetCarsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarsViewModel @Inject constructor(
    private val getCars: GetCarsUseCase,
) : ViewModel() {

    private sealed interface UserAction {
        object Retry : UserAction
        object Refresh : UserAction
    }

    private val userActions = MutableSharedFlow<UserAction>()
    private var currentState: CarsUiState = CarsUiState.Loading

    val uiState: StateFlow<CarsUiState> = flow {
        if (currentState is CarsUiState.Data) {
            // Do not hit api again when data is already loaded and emit cached data
            emit(currentState)
        } else {
            emit(CarsUiState.Loading)
            emit(getCarsState())
        }
        userActions.collect { event ->
            when (event) {
                UserAction.Refresh -> {
                    val dataState = currentState as? CarsUiState.Data
                    val refreshingState = dataState?.copy(isRefreshing = true)
                    emit(refreshingState ?: CarsUiState.Loading)
                    emit(getCarsState())
                }

                UserAction.Retry -> {
                    emit(CarsUiState.Loading)
                    emit(getCarsState())
                }
            }
        }
    }.distinctUntilChanged().onEach {
        // cache current state
        currentState = it
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(0),
        initialValue = currentState
    )

    private suspend fun getCarsState() = when (val result = getCars()) {
        is Result.Error -> CarsUiState.Error
        is Result.Success -> CarsUiState.Data(
            cars = result.data.map { it.toCarUiModel() },
            isRefreshing = false
        )
    }

    fun refresh() {
        viewModelScope.launch { userActions.emit(UserAction.Refresh) }
    }

    fun retry() {
        viewModelScope.launch { userActions.emit(UserAction.Retry) }
    }
}