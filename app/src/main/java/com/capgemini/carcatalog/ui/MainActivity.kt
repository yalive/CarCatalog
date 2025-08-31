package com.capgemini.carcatalog.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.capgemini.carcatalog.R
import com.capgemini.carcatalog.ui.cars.CarsScreen
import com.capgemini.carcatalog.ui.theme.CarCatalogTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CarCatalogTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Surface(shadowElevation = 6.dp) {
                            CenterAlignedTopAppBar(
                                title = { Text(stringResource(R.string.cars_screen_title)) }
                            )
                        }

                    }
                ) { innerPadding ->
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