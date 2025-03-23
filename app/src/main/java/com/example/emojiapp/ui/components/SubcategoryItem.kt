package com.example.emojiapp.ui.components

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
import com.example.emojiapp.ui.utils.Constants

/**
 * Komponent wyświetlający pojedynczą podkategorię.
 */
@Composable
fun SubcategoryItem(subcategory: String, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                navController.navigate("${Constants.CONFIRMATION_ROUTE}$subcategory")
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = subcategory,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}