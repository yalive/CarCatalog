package com.capgemini.carcatalog.common

import com.capgemini.carcatalog.data.repository.CarCatalogRepository
import com.capgemini.carcatalog.data.service.CarCatalogService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ManualDI {

    val carCatalogRepository by lazy {
        CarCatalogRepository(
            carCatalogService = retrofit.create<CarCatalogService>(),
            networkRunner = networkRunner
        )
    }

    val networkRunner by lazy { NetworkRunner() }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.carqueryapi.com/api/0.3/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
}