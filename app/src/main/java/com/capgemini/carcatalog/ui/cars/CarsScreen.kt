package com.capgemini.carcatalog.ui.cars

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.capgemini.carcatalog.R


@Composable
fun CarsScreen(
    modifier: Modifier = Modifier,
    uiState: CarsUiState,
    onRefresh: () -> Unit,
    onRetry: () -> Unit,
) {
    Box(modifier = modifier) {
        when (uiState) {
            is CarsUiState.Data -> CarListing(
                cars = uiState.cars,
                isRefreshing = uiState.isRefreshing,
                onRefresh = onRefresh
            )

            CarsUiState.Error -> CarsError(
                modifier = Modifier.align(Alignment.Center),
                onRetry = onRetry
            )

            CarsUiState.Loading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarListing(
    cars: List<CarUiModel>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 56.dp
            ),
        ) {
            items(cars.size) { index ->
                CarItem(car = cars[index])
            }
        }
    }
}

@Composable
private fun CarItem(
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

@Composable
private fun CarsError(
    modifier: Modifier,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error occurred while loading cars!\nPlease try again",
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))

        Button(onClick = onRetry) {
            Text("Try again")
        }
    }
}