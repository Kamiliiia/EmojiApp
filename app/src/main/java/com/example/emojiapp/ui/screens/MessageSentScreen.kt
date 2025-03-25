package com.example.emojiapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.emojiapp.ui.utils.Constants.START_ROUTE

/**
 * Ekran wyświetlany po wysłaniu wiadomości.
 * Pokazuje komunikat o wysłaniu wiadomości i umożliwia powrót do ekranu startowego.
 */
@Composable
fun MessageSentScreen(navController: NavHostController) { // Dodano navController
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                navController.navigate(START_ROUTE) {
                    popUpTo(START_ROUTE) { inclusive = true }
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Wiadomość wysłana!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = "kliknij aby kontynuować",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}