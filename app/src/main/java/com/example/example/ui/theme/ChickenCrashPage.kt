package com.example.example.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChickenCrashPage(score: Int, onScoreChange: (Int) -> Unit, onBack: () -> Unit) {
    var bet by remember { mutableStateOf("") }
    var gameStarted by remember { mutableStateOf(false) }
    var currentMultiplier by remember { mutableStateOf(1) }
    var currentWinnings by remember { mutableStateOf(0) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Score: $score", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Current Winnings: $currentWinnings", style = MaterialTheme.typography.bodyLarge)

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val baseBet = bet.toIntOrNull() ?: 0
                if (gameStarted && baseBet > 0) {
                    val lose = (0..2).random() == 0
                    if (lose) {
                        currentWinnings = 0
                        gameStarted = false
                        errorMessage = "Perdiste!"
                    } else {
                        currentMultiplier *= 2
                        currentWinnings *= 2
                        errorMessage = ""
                    }
                }
            },
            enabled = gameStarted,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("FLIP!", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = bet,
                onValueChange = {
                    bet = it
                    errorMessage = ""
                },
                label = { Text("Bet") },
                enabled = !gameStarted,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    val baseBet = bet.toIntOrNull() ?: 0
                    if (!gameStarted) {
                        if (baseBet <= 0) {
                            errorMessage = "Error"
                        } else if (baseBet > score) {
                            errorMessage = "Error"
                        } else {
                            onScoreChange(score - baseBet)
                            currentMultiplier = 1
                            currentWinnings = baseBet
                            gameStarted = true
                            errorMessage = ""
                        }
                    } else {
                        onScoreChange(score + currentWinnings)
                        currentWinnings = 0
                        gameStarted = false
                        errorMessage = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(if (gameStarted) "Stop" else "Start")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}


