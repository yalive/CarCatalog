package com.capgemini.carcatalog.domain

import com.capgemini.carcatalog.common.Result

interface CarCatalogRepository {

    suspend fun getCars(): Result<List<CarModel>>

}