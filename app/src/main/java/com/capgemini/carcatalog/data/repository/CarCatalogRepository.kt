package com.capgemini.carcatalog.data.repository

import com.capgemini.carcatalog.common.NetworkRunner
import com.capgemini.carcatalog.common.Result
import com.capgemini.carcatalog.data.model.CarRS
import com.capgemini.carcatalog.data.service.CarCatalogService
import javax.inject.Inject

class CarCatalogRepository @Inject constructor(
    private val carCatalogService: CarCatalogService,
    private val networkRunner: NetworkRunner
) {

    suspend fun getCars(): Result<List<CarRS>> {
        return networkRunner.execute {
            carCatalogService.getCars().models.orEmpty()
        }
    }
}