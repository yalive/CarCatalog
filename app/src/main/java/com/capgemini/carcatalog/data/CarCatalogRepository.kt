package com.capgemini.carcatalog.data

class CarCatalogRepository(
    private val carCatalogService: CarCatalogService
) {

    suspend fun getCars(): List<CarRS> {
        return carCatalogService.getCars().models.orEmpty()
    }
}