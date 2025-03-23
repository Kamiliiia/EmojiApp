package com.example.emojiapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.emojiapp.ui.utils.Constants.MAIN_ROUTE
import com.example.emojiapp.ui.utils.Constants.SETTINGS_ROUTE

/**
 * Ekran startowy aplikacji.
 * Wyświetla ikonę domku i tekst zachęcający do kliknięcia.
 */
@Composable
fun StartScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { navController.navigate(MAIN_ROUTE) },
        contentAlignment = Alignment.Center
    ) {
        // Ustawienia (ikona)
        IconButton(
            onClick = { navController.navigate(SETTINGS_ROUTE) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Ustawienia",
                modifier = Modifier.size(32.dp)
            )
        }
        // Domek i napis
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = "Menu Główne",
                modifier = Modifier.size(150.dp)
            )
            Text(
                text = "Kliknij w domek aby kontynuować",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}