package com.capgemini.carcatalog.data.service

import com.capgemini.carcatalog.data.model.CarListRS
import retrofit2.http.GET

interface CarCatalogService {

    @GET("?cmd=getModels&make=volkswagen")
    suspend fun getCars(): CarListRS
}