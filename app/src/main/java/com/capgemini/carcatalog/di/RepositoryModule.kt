package com.capgemini.carcatalog.di

import com.capgemini.carcatalog.data.repository.CarCatalogRepositoryImpl
import com.capgemini.carcatalog.domain.CarCatalogRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCarCatalogRepository(
        impl: CarCatalogRepositoryImpl
    ): CarCatalogRepository
}