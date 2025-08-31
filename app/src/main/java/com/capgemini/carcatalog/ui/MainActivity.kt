package com.capgemini.carcatalog.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.capgemini.carcatalog.ui.cars.CarsScreen
import com.capgemini.carcatalog.ui.theme.CarCatalogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarCatalogTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CarsScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                    )
                }
            }
        }
    }
}