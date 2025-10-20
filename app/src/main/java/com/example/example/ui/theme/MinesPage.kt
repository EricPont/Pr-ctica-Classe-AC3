package com.example.example.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MinesPage(score: Int, onScoreChange: (Int) -> Unit, onBack: () -> Unit) {
    var bet by remember { mutableStateOf("") }
    var betAmount by remember { mutableStateOf(0) }
    var gameStarted by remember { mutableStateOf(false) }
    var currentWinnings by remember { mutableStateOf(0f) }
    var bombIndex by remember { mutableStateOf(-1) }
    var errorMessage by remember { mutableStateOf("") }
    val revealed = remember { mutableStateListOf<Boolean>().apply { repeat(9) { add(false) } } }

    fun revealedCount(): Int = revealed.count { it }

    fun multiplierForCurrentState(): Float {
        val k = revealedCount()
        val denom = 9 - k
        return if (denom > 0) 9f / denom.toFloat() else 9f
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Score: $score", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Winnings: ${currentWinnings.roundToInt()}", style = MaterialTheme.typography.bodyLarge)

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = MaterialTheme.colorScheme.error)
            Spacer(modifier = Modifier.height(8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Row(modifier = Modifier.fillMaxWidth()){
            }
            for (row in 0 until 3) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (col in 0 until 3) {
                        val index = row * 3 + col
                        Button(
                            onClick = {
                                if (gameStarted && !revealed[index]) {
                                    val betValue = betAmount
                                    revealed[index] = true
                                    if (index == bombIndex) {
                                        errorMessage = "Perdiste!"
                                        gameStarted = false
                                        currentWinnings = 0f
                                    } else {
                                        val multiplier = multiplierForCurrentState()
                                        currentWinnings = betValue * multiplier
                                        errorMessage = ""
                                        if (revealedCount() == 8) {
                                            onScoreChange(score + currentWinnings.toInt())
                                            gameStarted = false
                                        }
                                    }
                                }
                            },
                            enabled = gameStarted && !revealed[index],
                            colors = ButtonDefaults.buttonColors(
                                containerColor = when {
                                    revealed[index] && index == bombIndex -> MaterialTheme.colorScheme.error
                                    revealed[index] -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.secondary
                                }
                            ),
                            modifier = Modifier.size(80.dp)
                        ) {
                            Text(
                                text = when {
                                    revealed[index] && index == bombIndex -> "O"
                                    revealed[index] -> {
                                        val mult = multiplierForCurrentState()
                                        if (mult % 1f == 0f) "${mult.toInt()}x" else String.format("%.2fx", mult)
                                    }
                                    else -> ""
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedTextField(
                value = bet,
                onValueChange = {

                    bet = it.filter { ch -> ch.isDigit() }
                    errorMessage = ""
                },
                label = { Text("Bet") },
                enabled = !gameStarted,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    val betValue = bet.toIntOrNull() ?: 0

                    if (!gameStarted) {
                        if (betValue <= 0) {
                            errorMessage = "Eror"
                            return@Button
                        } else if (betValue > score) {
                            errorMessage = "Error"
                            return@Button
                        }

                        betAmount = betValue
                        bombIndex = (0..8).random()
                        for (i in 0..8) revealed[i] = false
                        onScoreChange(score - betValue)
                        gameStarted = true
                        errorMessage = ""
                        currentWinnings = betValue.toFloat()
                    } else {

                        gameStarted = false
                        onScoreChange(score + currentWinnings.toInt())
                        errorMessage = ""
                        currentWinnings = 0f
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






