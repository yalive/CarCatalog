package com.capgemini.carcatalog.domain

import com.capgemini.carcatalog.common.Result
import javax.inject.Inject

class GetCarsUseCase @Inject constructor(
    private val carCatalogRepository: CarCatalogRepository
) {

    suspend operator fun invoke(): Result<List<CarModel>> {
        return carCatalogRepository.getCars()
    }
}