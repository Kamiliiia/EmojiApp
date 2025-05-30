package com.example.emojiapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.emojiapp.BLEManager // Zaimportuj BLEManager
import com.example.emojiapp.ui.utils.Constants.MAIN_ROUTE
import com.example.emojiapp.ui.utils.Constants.MESSAGE_SENT_ROUTE
import java.util.UUID // Potrzebne dla UUID

/**
 * Ekran potwierdzenia wysłania wiadomości.
 * Wyświetla nazwę podkategorii i przyciski do potwierdzenia.
 */
@Composable
fun ConfirmationScreen(
    subcategory: String,
    navController: NavHostController,
    bleManager: BLEManager // Dodano parametr bleManager
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Czy na pewno chcesz wysłać: $subcategory?",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = {
                // 1. Wyślij wiadomość przez BLE
                // Załóżmy, że masz zdefiniowane UUID serwisu i charakterystyki
                // oraz że subcategory to String, który chcesz wysłać jako bajty
                val messageBytes = subcategory.toByteArray(Charsets.UTF_8) // Konwertuj String na ByteArray

                bleManager.writeCharacteristic(messageBytes)

                // 2. Nawiguj do ekranu potwierdzenia wysłania
                navController.navigate(MESSAGE_SENT_ROUTE)
            }) {
                Text("Tak")
            }
            Button(onClick = { navController.navigate(MAIN_ROUTE) }) {
                Text("Nie")
            }
        }
    }
}