package com.example.vacation_advisor

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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

    // Use a Box with constrained height for scrollable content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFf1c40f), // Tropical yellow
                        Color(0xFFf39c12), // Orange
                        Color(0xFFe74c3c)  // Warm red
                    )
                )
            )
            .paint(
                painter = painterResource(id = R.drawable.vacation_background), // Correct resource reference
                contentScale = ContentScale.Crop
            )
    ) {
        // Constrain height to avoid infinite height issues
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp)
                .verticalScroll(rememberScrollState())
                .heightIn(max = 900.dp) // Constrain height here
        ) {
            // Title Panel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp)
            ) {
                Text(
                    text = "Vacation Destination Advisor",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Slider Panel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Select Month", style = MaterialTheme.typography.bodyLarge)
                    Slider(
                        value = month.toFloat(),
                        onValueChange = { month = it.toInt() },
                        valueRange = 1f..12f,
                        steps = 11
                    )
                    Text("Month: $month")
                }
            }

            // Budget Panel
            // Budget Panel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center // Center everything vertically and horizontally
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Select Budget", style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(8.dp))

                    // Center the Text and make it look like a dropdown field
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f) // Make the dropdown box smaller and centered
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(MaterialTheme.colorScheme.background)
                            .clickable { expanded = !expanded }
                            .padding(8.dp)
                    ) {
                        Text(
                            text = budget,
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    // Dropdown menu that matches the width of the dropdown box
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.width(180.dp) // Make sure the width is similar to the dropdown box
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
            }



            // Type Options Panel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(4.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(), // Ensure the entire column takes the full width
                    horizontalAlignment = Alignment.Start // Align radio buttons to the start
                ) {
                    // Center the "Select Type" text
                    Box(
                        modifier = Modifier.fillMaxWidth(), // Take full width to allow text to be centered
                        contentAlignment = Alignment.Center // Center the title text
                    ) {
                        Text("Select Type", style = MaterialTheme.typography.bodyLarge)
                    }

                    // Keep the radio buttons aligned to the start
                    typeOptions.forEach { text ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = (text == typeSelectedOption),
                                onClick = { typeSelectedOption = text }
                            )
                            Text(text)
                        }
                    }
                }

            }

            // Centered Button Panel
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        dialogStatus = true
                    }
                ) {
                    Text("Find My Destination")
                }
            }

            val monthMap = mapOf(
                1 to "January",
                2 to "February",
                3 to "March",
                4 to "April",
                5 to "May",
                6 to "June",
                7 to "July",
                8 to "August",
                9 to "September",
                10 to "October",
                11 to "November",
                12 to "December"
            )

            // Alert Dialog
            if (dialogStatus) {
                AlertDialog(
                    onDismissRequest = { dialogStatus = true },
                    title = { Text("AI is not perfect") },
                    text = { Text("Gemini can make mistakes. Check important info.") },
                    confirmButton = {
                        Button(
                            onClick = {
                                dialogStatus = false

                                // Get the month name from the map using the month integer
                                val monthName = monthMap[month] ?: "Unknown month" // Fallback in case month is out of range

                                prompt =
                                    "I want to go on a $budget vacation in $monthName to a $typeSelectedOption destination. " +
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

            // Result Panel
            if (uiState is UiState.Loading)
            {
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

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                        .verticalScroll(scrollState)
                        .background(
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(4.dp)
                ) {
                    val cityMatch = Regex("\\*\\*(.*?)\\*\\*").find(result)
                    val cityName = cityMatch?.groupValues?.get(1) ?: ""
                    result = result.replace("**$cityName**", cityName)

                    Text(
                        text = cityName,
                        textAlign = TextAlign.Center,
                        color = textColor,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Text(
                        text = result,
                        textAlign = TextAlign.Start,
                        color = textColor,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
