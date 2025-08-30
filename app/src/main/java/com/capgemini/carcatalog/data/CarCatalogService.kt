package com.capgemini.carcatalog.data

import retrofit2.http.GET

interface CarCatalogService {

    @GET("?cmd=getModels&make=ford")
    suspend fun getCars(): CarListRS
}