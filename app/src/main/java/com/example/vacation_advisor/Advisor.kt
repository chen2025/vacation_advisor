package com.example.vacation_advisor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun FindDestination(
    bakingViewModel: BakingViewModel = viewModel()
) {
    var prompt by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf("") }
    var month by rememberSaveable { mutableIntStateOf(1) }
    var expanded by remember { mutableStateOf(false) }
    var budget by remember { mutableStateOf("Economy") }
    val budgetOptions = listOf("Economy", "Moderate", "Comfort", "Luxury")
    val typeOptions = listOf("Beach", "Adventure", "Cultural & Historical", "Wellness", "City Life")
    var typeSelectedOption by remember { mutableStateOf(typeOptions[0]) }
    var dialogStatus by remember { mutableStateOf(false) }
    val uiState by bakingViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.padding(32.dp)
    ) {
        Text(
            text = "Vacation Destination Advisor",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(32.dp)
        )

        Slider(
            value = month.toFloat(),
            onValueChange = { month = it.toInt() },
            valueRange = 1f..12f,
            steps = 11
        )
        Text("Month: $month")

        Box {
            Text(budget, modifier = Modifier.clickable { expanded = !expanded })
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                budgetOptions.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption) },
                        onClick = {
                            budget = selectionOption
                            expanded = false
                        }
                    )
                }
            }
        }

        typeOptions.forEach { text ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (text == typeSelectedOption),
                    onClick = { typeSelectedOption = text }
                )
                Text(text)
            }
        }

        Button(
            onClick = {
                dialogStatus = true

            },
        ) {
            Text("Find My Destination")
        }

        // display an alert dialog to warn the user about AI limitations (Gemini can make mistakes. Check important info.)
        if (dialogStatus) {
            AlertDialog(
                onDismissRequest = { dialogStatus = true },
                title = { Text("AI is not perfect") },
                text = {
                    Text(
                        "Gemini can make mistakes. Check important info."
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            dialogStatus = false
                            prompt =
                                "I want to go on a $budget vacation in $month to a $typeSelectedOption destination. " +
                                        "What is the best US city to visit? Talk about why you chose it for this specific month. " +
                                        "Limit your answer to 100 words or less. " +
                                        "Start your response with the city name in between ** and **."
                            bakingViewModel.sendPrompt(prompt)
                        }
                    ) {
                        Text("I understand")
                    }
                }
            )
        }

        if (uiState is UiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            var textColor = MaterialTheme.colorScheme.onSurface
            if (uiState is UiState.Error) {
                textColor = MaterialTheme.colorScheme.error
                result = (uiState as UiState.Error).errorMessage
            } else if (uiState is UiState.Success) {
                textColor = MaterialTheme.colorScheme.onSurface
                result = (uiState as UiState.Success).outputText
            }
            val scrollState = rememberScrollState()

            // parse the response to extract the city name
            // city name is in between ** and **
            val cityMatch = Regex("\\*\\*(.*?)\\*\\*").find(result)
            val cityName = cityMatch?.groupValues?.get(1) ?: ""
            // remove ** and ** from the result
            result = result.replace("**$cityName**", cityName)
            Text(
                text = cityName,
                textAlign = TextAlign.Start,
                color = textColor,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = result,
                textAlign = TextAlign.Start,
                color = textColor,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            )
        }
    }
}