package com.capgemini.carcatalog.data.repository

import com.capgemini.carcatalog.common.NetworkRunner
import com.capgemini.carcatalog.common.Result
import com.capgemini.carcatalog.data.model.toCarModel
import com.capgemini.carcatalog.data.service.CarCatalogService
import com.capgemini.carcatalog.domain.CarCatalogRepository
import com.capgemini.carcatalog.domain.CarModel
import javax.inject.Inject

class CarCatalogRepositoryImpl @Inject constructor(
    private val carCatalogService: CarCatalogService,
    private val networkRunner: NetworkRunner
) : CarCatalogRepository {

    override suspend fun getCars(): Result<List<CarModel>> {
        return networkRunner.execute {
            carCatalogService.getCars().models.orEmpty().map { it.toCarModel() }
        }
    }
}