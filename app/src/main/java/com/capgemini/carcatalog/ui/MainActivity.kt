package com.capgemini.carcatalog.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.capgemini.carcatalog.common.ManualDI
import com.capgemini.carcatalog.ui.cars.CarsScreen
import com.capgemini.carcatalog.ui.cars.MainViewModel
import com.capgemini.carcatalog.ui.cars.MainViewModel.Companion.CarCatalogRepositoryKey
import com.capgemini.carcatalog.ui.theme.CarCatalogTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>(
        factoryProducer = { MainViewModel.factory },
        extrasProducer = {
            MutableCreationExtras().apply {
                set(CarCatalogRepositoryKey, ManualDI.carCatalogRepository)
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarCatalogTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    CarsScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        uiState = uiState,
                        onRefresh = viewModel::refresh,
                        onRetry = viewModel::retry
                    )
                }
            }
        }
    }
}