package com.quilter.bookapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.quilter.bookapplication.presentation.ui.screen.bookscreen.BookScreen
import com.quilter.bookapplication.presentation.ui.theme.BookApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            BookApplicationTheme {
                BookScreen()
            }
        }
    }
}