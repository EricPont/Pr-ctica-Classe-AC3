package com.example.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.example.ui.theme.ChickenCrashPage
import com.example.example.ui.theme.FrontPage
import com.example.example.ui.theme.MinesPage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                var currentScreen by remember { mutableStateOf("front") }
                var score by remember { mutableStateOf(1000) }

                when (currentScreen) {
                    "front" -> FrontPage(
                        onNavigate = { screen -> currentScreen = screen },
                        score = score
                    )
                    "page1" -> MinesPage(
                        score = score,
                        onScoreChange = { score = it },
                        onBack = { currentScreen = "front" }
                    )
                    "page2" -> ChickenCrashPage(
                        score = score,
                        onScoreChange = { score = it },
                        onBack = { currentScreen = "front" }
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MaterialTheme {

        var currentScreen by remember { mutableStateOf("front") }
        var score by remember { mutableStateOf(1000) }

        when (currentScreen) {
            "front" -> FrontPage(
                onNavigate = { screen -> currentScreen = screen },
                score = score
            )
            "page1" -> MinesPage(
                score = score,
                onScoreChange = { score = it },
                onBack = { currentScreen = "front" }
            )
            "page2" -> ChickenCrashPage(
                score = score,
                onScoreChange = { score = it },
                onBack = { currentScreen = "front" }
            )
        }
    }
}