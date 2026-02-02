package com.quilter.bookapplication.presentation.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

@Composable
fun <T : Any> PagingStateHandler(
    pagingItems: LazyPagingItems<T>,
    content: @Composable () -> Unit
) {
    val refreshState = pagingItems.loadState.refresh
    val isEmpty = pagingItems.itemCount == 0

    when (refreshState) {
        is LoadState.Loading -> {
            if (isEmpty) {
                // Initial load
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                // Refreshing
                content()
            }
        }

        is LoadState.Error -> {
            if (isEmpty) {
                // TODO JIMMY
            } else {
                content()
            }
        }

        is LoadState.NotLoading -> {
            if (isEmpty) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No books found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                content()
            }
        }
    }
}