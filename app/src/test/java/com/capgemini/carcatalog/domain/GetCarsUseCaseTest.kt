package com.capgemini.carcatalog.domain

import com.capgemini.carcatalog.common.Result
import com.capgemini.carcatalog.data.repository.CarCatalogRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCarsUseCaseTest {

    private val catalogRepository = mockk<CarCatalogRepository>()

    @Test
    fun `Cars loaded successfully`() = runTest {
        // Given
        val useCase = GetCarsUseCase(catalogRepository)
        coEvery { catalogRepository.getCars() } returns Result.Success(CARS)

        // When
        val result = useCase.invoke()

        // Then
        assertEquals(CARS, (result as Result.Success).data)
    }

    @Test
    fun `Error occurred while loading cars`() = runTest {
        // Given
        val useCase = GetCarsUseCase(catalogRepository)
        coEvery { catalogRepository.getCars() } returns Result.Error(Exception())

        // When
        val result = useCase.invoke()

        // Then
        assertTrue(result is Result.Error)
    }
}

private val CARS = listOf(
    CarModel("Model 1", "Desc 1", "image1.jpg"),
    CarModel("Model 2", "Desc 2", "image2.jpg"),
    CarModel("Model 3", "Desc 3", "image3.jpg"),
)