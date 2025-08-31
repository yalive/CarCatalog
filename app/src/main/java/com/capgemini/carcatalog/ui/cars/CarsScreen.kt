package com.capgemini.carcatalog.ui.cars

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage


@Composable
fun CarsScreen(
    modifier: Modifier = Modifier,
    viewModel: CarsViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CarsScreenContent(
        modifier = modifier,
        uiState = uiState,
        onRefresh = viewModel::refresh,
        onRetry = viewModel::retry
    )
}

@Composable
private fun CarsScreenContent(
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
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                    thickness = 0.3.dp,
                )
            }
        }
    }
}

@Composable
private fun CarItem(
    car: CarUiModel
) {
    Row {
        AsyncImage(
            model = car.imageUrl,
            contentDescription = null,
            modifier = Modifier.size(56.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(text = car.name, fontSize = 18.sp)
            Text(text = car.description, fontSize = 12.sp, fontWeight = FontWeight.Thin)
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