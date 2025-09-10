package com.capgemini.carcatalog.data.repository

import com.capgemini.carcatalog.common.NetworkRunner
import com.capgemini.carcatalog.common.Result
import com.capgemini.carcatalog.data.model.CarListRS
import com.capgemini.carcatalog.data.model.CarRS
import com.capgemini.carcatalog.data.service.CarCatalogService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.net.UnknownHostException

class CarCatalogRepositoryTest {

    private val carCatalogService = mockk<CarCatalogService>()

    @Test
    fun `Cars loaded successfully`() = runTest {
        // Given
        val repository = CarCatalogRepositoryImpl(carCatalogService, NetworkRunner())
        coEvery { carCatalogService.getCars() } returns CarListRS(CARS)

        // When
        val result = repository.getCars()

        // Then
        Assert.assertTrue(result is Result.Success)
    }

    @Test
    fun `Error loading cars`() = runTest {
        // Given
        val repository = CarCatalogRepositoryImpl(carCatalogService, NetworkRunner())
        coEvery { carCatalogService.getCars() } throws UnknownHostException("No internet")

        // When
        val result = repository.getCars()

        // Then
        Assert.assertTrue(result is Result.Error)
    }
}

private val CARS = listOf(
    CarRS("Model 1", "Desc 1"),
    CarRS("Model 2", "Desc 2"),
    CarRS("Model 3", "Desc 3"),
)