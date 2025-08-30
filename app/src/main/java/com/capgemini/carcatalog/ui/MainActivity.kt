package com.capgemini.carcatalog.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.MutableCreationExtras
import com.capgemini.carcatalog.ManualDI
import com.capgemini.carcatalog.R
import com.capgemini.carcatalog.ui.MainViewModel.Companion.CarCatalogRepositoryKey
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

                    val cars by viewModel.cars.collectAsStateWithLifecycle()

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            top = 16.dp,
                            end = 16.dp,
                            bottom = 56.dp
                        ),
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        items(cars.size) { index ->
                            CarItem(car = cars[index])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CarItem(
    car: CarUiModel
) {
    Row {
        Image(
            painter = painterResource(R.drawable.app_logo),
            contentDescription = null,
            modifier = Modifier.size(56.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(text = car.name)
            Text(text = car.description)
        }
    }
}