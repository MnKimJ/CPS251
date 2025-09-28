package com.example.assignment3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Text
import androidx.compose.ui.draw.alpha

/**
 * This project covers concepts from Chapter 6 lessons:
 * - "Understanding State in Compose" - for state management and updates
 * - "Stateless and Stateful Composables" - for component design patterns
 * - "Launched Effect" - for side effects and timers
 *
 * Students should review these lessons before starting:
 * - Understanding State in Compose lesson for state management
 * - Stateless and Stateful Composables lesson for component patterns
 * - Launched Effect lesson for side effects and timers
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                StudyTimerApp()
            }
        }
    }
}

@Composable
fun StudyTimerApp() {
    var isRunning by remember { mutableStateOf(false) }
    var sessionLength by remember { mutableStateOf(25) }
    var timeRemaining by remember { mutableStateOf(sessionLength * 60) }
    var completedSessions by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimerDisplay(timeRemaining = timeRemaining, sessionLength = sessionLength)


        Spacer(modifier = Modifier.height(32.dp))
        TimerControls(
            isRunning = isRunning,
            onToggleTimer = {
                if (isRunning) {
                    isRunning = false
                    timeRemaining = sessionLength * 60
                } else {
                    isRunning = true
                }
            }
        )
            
        }

        Spacer(modifier = Modifier.height(32.dp))

        SessionSettings(
            sessionLength = sessionLength,
            onSessionLengthChange = {
                length ->
                isRunning = false
                sessionLength = length
                timeRemaining = length * 60
            }
            )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Completed Sessions: $completedSessions",
            modifier = Modifier.offset(y=320.dp).fillMaxWidth(),
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold

        )
        LaunchedEffect(isRunning) {
            while (isRunning){
                delay(1000)
                timeRemaining -= 1
            }
            if(timeRemaining == 0){
                isRunning = false
                completedSessions += 1
            }
        }
    }





@Composable
fun TimerDisplay(
    timeRemaining: Int,
    sessionLength: Int
) {

    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val progress = 1f - (timeRemaining.toFloat() / (sessionLength * 60))

    Text(
        text = "Study Timer",
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = String.format("%02d:%02d", minutes, seconds),
        fontSize = 50.sp,
        fontWeight = FontWeight.Bold
    )
    Text(
        text = "${(progress * 100).toInt()}% Complete",
        fontSize = 15.sp
    )
}

@Composable
fun TimerControls(
    isRunning: Boolean,
    onToggleTimer: () -> Unit
) {
    var isCurrentlyRunning by remember { mutableStateOf(isRunning) }
    Button(
        onClick = {
            isCurrentlyRunning = !isCurrentlyRunning
            onToggleTimer()
        },
        modifier = Modifier
            .width(100.dp)
            .height(50.dp)
        ){
        Text(
            text =
                if(isCurrentlyRunning)
                    "Reset"
                else
                    "Start"
        )

    }


}

@Composable
fun SessionSettings(
    sessionLength: Int,
    onSessionLengthChange: (Int) -> Unit

) {
    Text(
        text = "Session Length: $sessionLength minutes",
        modifier = Modifier
            .offset(y = 240.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
    Row(
        modifier = Modifier.fillMaxWidth().offset(y = 265.dp),
        horizontalArrangement = Arrangement.Center,

    ){
    val lengths = listOf(5,15,25,45)

    lengths.forEach { minutes ->
        val chosen = sessionLength == minutes
        Button(
            onClick = { onSessionLengthChange(minutes) },
            modifier = Modifier
                .width(70.dp)
                .padding(horizontal = 2.5.dp)
                .alpha(alpha =
                    if (chosen) {
                        1f
                    }
                    else
                        0.3f
                )
        ) {
            Text(
                text = "${minutes}"
            )
        }
    }
    }
    // TODO: Create session length configuration
    // Allow users to set custom session lengths (5, 15, 25, 45 minutes)
    // Use Button composables for each option
    // Highlight the currently selected length
    // Display the current session length value
    // Make sure the buttons show the correct minute values: 5m, 15m, 25m, 45m
    // Use appropriate button sizing (width = 70.dp) to display text properly
    // See "Understanding State in Compose" lesson for examples of state management and updates
}


@Preview(showBackground = true)
@Composable
fun StudyTimerPreview() {
    StudyTimerApp()
}