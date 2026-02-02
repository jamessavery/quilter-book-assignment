package com.quilter.bookapplication.presentation.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import retrofit2.HttpException
import java.io.IOException

@Composable
fun GenericErrorScreen(
    error: Throwable,
    onRetry: () -> Unit
) {
    val message = rememberErrorMessage(error)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Oops!",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Try Again")
        }
    }
}

// D_N On prod, this goes against clean architecture as Throwable has no business being on this layer,
// This couples the presentation layer with the data, but for the purposes of this assignment..
@Composable
fun rememberErrorMessage(error: Throwable): String {
    return when (error) {
        is IOException -> "No internet connection. Please check your network."
        is HttpException -> "Server error (${error.code()}). Please try again later."
        else -> error.localizedMessage ?: "Something went wrong."
    }
}
