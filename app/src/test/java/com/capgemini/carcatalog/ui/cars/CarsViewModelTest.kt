package com.capgemini.carcatalog.ui.cars

import com.capgemini.carcatalog.MainDispatcherRule
import com.capgemini.carcatalog.common.Result
import com.capgemini.carcatalog.domain.CarModel
import com.capgemini.carcatalog.domain.GetCarsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CarsViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val getCarsUseCase = mockk<GetCarsUseCase>()

    @Test
    fun `Cars loaded successfully`() = runTest {
        // Given
        val viewModel = CarsViewModel(getCarsUseCase)
        coEvery { getCarsUseCase.invoke() } returns Result.Success(CARS)

        // When
        val items = mutableListOf<CarsUiState>()
        backgroundScope.launch(dispatcherRule.testDispatcher) {
            viewModel.uiState.toList(items)
        }

        // Then
        assertTrue(items.size == 2)
        assertTrue(items[0] is CarsUiState.Loading)
        val dataState = items[1] as CarsUiState.Data
        assertEquals(CARS_UI_MODELS, dataState.cars)
        assertFalse(dataState.isRefreshing)
    }

    @Test
    fun `Error loading cars`() = runTest {
        // Given
        val viewModel = CarsViewModel(getCarsUseCase)
        coEvery { getCarsUseCase.invoke() } returns Result.Error(Exception())

        // When
        val items = mutableListOf<CarsUiState>()
        backgroundScope.launch(dispatcherRule.testDispatcher) {
            viewModel.uiState.toList(items)
        }

        // Then
        assertTrue(items.size == 2)
        assertTrue(items[0] is CarsUiState.Loading)
        assertTrue(items[1] is CarsUiState.Error)
    }

    @Test
    fun `Refresh emits refreshing then new data`() = runTest {
        // Given
        val viewModel = CarsViewModel(getCarsUseCase)
        coEvery { getCarsUseCase.invoke() } coAnswers {
            delay(1) // ensure refreshing is observable
            Result.Success(CARS)
        }
        val items = mutableListOf<CarsUiState>()
        backgroundScope.launch(dispatcherRule.testDispatcher) {
            viewModel.uiState.toList(items)
        }
        advanceUntilIdle() // initial load done

        // When
        viewModel.refresh()
        advanceUntilIdle()

        // Then Expect 4 states: Loading, Data(CARS,false), Data(CARS,true), Data(CARS,false)
        assertEquals(4, items.size)
        assertTrue(items[0] is CarsUiState.Loading)
        val firstData = items[1] as CarsUiState.Data
        assertFalse(firstData.isRefreshing)
        val refreshing = items[2] as CarsUiState.Data
        assertTrue(refreshing.isRefreshing)
        val secondData = items[3] as CarsUiState.Data
        assertFalse(secondData.isRefreshing)
    }

    @Test
    fun `Retry emits loading then new data`() = runTest {
        // Given
        val viewModel = CarsViewModel(getCarsUseCase)
        coEvery { getCarsUseCase.invoke() } coAnswers {
            Result.Error(Exception())
        } coAndThen {
            delay(1)
            Result.Success(CARS)
        }
        val items = mutableListOf<CarsUiState>()
        backgroundScope.launch(dispatcherRule.testDispatcher) {
            viewModel.uiState.toList(items)
        }
        advanceUntilIdle() // initial load done

        // When
        viewModel.retry()
        advanceUntilIdle()

        // Expect 4 states: Loading, ERROR, Loading, Data(CARS,false)
        assertEquals(4, items.size)
        assertTrue(items[0] is CarsUiState.Loading)
        assertTrue(items[1] is CarsUiState.Error)
        assertTrue(items[2] is CarsUiState.Loading)
        val data = items[3] as CarsUiState.Data
        assertFalse(data.isRefreshing)
    }
}

private val CARS = listOf(
    CarModel("Model 1", "Desc 1", "image1.jpg"),
    CarModel("Model 2", "Desc 2", "image2.jpg"),
    CarModel("Model 3", "Desc 3", "image3.jpg"),
)

private val CARS_UI_MODELS = CARS.map { it.toCarUiModel() }