package com.example.emojiapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController

/**
 * Ekran ustawień aplikacji.
 * Obecnie wyświetla tylko tekst "Ustawienia".
 */
@Composable
fun SettingsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ustawienia",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        // Tutaj możesz dodać więcej elementów UI dla ustawień
    }
}