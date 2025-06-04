package com.example.emojiapp.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.emojiapp.ui.utils.Constants

/**
 * Komponent wyświetlający pojedynczą podkategorię (ikona + nazwa), klikalny.
 */
@Composable
fun SubcategoryItem(subcategoryName: String, navController: NavHostController) {
    // Pobierz ikonę dla danej podkategorii
    val icon = Constants.SUBCATEGORY_ICONS[subcategoryName]

    Column(
        modifier = Modifier
            .fillMaxSize() // Wypełnia stronę pagera
            .clickable {
                // Nawigacja do ekranu potwierdzenia z wybraną podkategorią
                navController.navigate("${Constants.CONFIRMATION_ROUTE}$subcategoryName")
            }
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = subcategoryName, // Opis dla dostępności
                modifier = Modifier.size(150.dp) // Duży rozmiar ikony, jak w CategoryScreen
                // Możesz dostosować ten rozmiar
            )
        } else {
            // Co zrobić, jeśli ikona nie zostanie znaleziona?
            // Można wyświetlić placeholder, tekst błędu lub nic.
            Log.w("SubcategoryItem", "Icon not found for subcategory: $subcategoryName")
            // Dla przykładu, wyświetlmy nazwę podkategorii, jeśli brakuje ikony
            Text(
                text = "$subcategoryName\n(brak ikony)",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Text(
            text = subcategoryName,
            style = MaterialTheme.typography.bodySmall, // Mały tekst pod ikoną
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}