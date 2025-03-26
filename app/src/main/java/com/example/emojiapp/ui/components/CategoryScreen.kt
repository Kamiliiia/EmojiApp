package com.example.emojiapp.ui.components

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
 * Komponent wyświetlający pojedynczą kategorię.
 */
@Composable
fun CategoryScreen(category: String, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                if (category == Constants.OTHER_SUBCATEGORY) {
                    navController.navigate("${Constants.CONFIRMATION_ROUTE}${Constants.OTHER_SUBCATEGORY}")
                } else {
                    navController.navigate("${Constants.SUBCATEGORIES_ROUTE}$category")
                }
            }
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (category == Constants.OTHER_SUBCATEGORY) {
            Icon(
                imageVector = Constants.CATEGORY_ICONS[category]!!,
                contentDescription = category,
                modifier = Modifier.size(150.dp),
            )
        } else {
            Icon(
                imageVector = Constants.CATEGORY_ICONS[category]!!,
                contentDescription = category,
                modifier = Modifier.size(150.dp)
            )
        }
        Text(
            text = category,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
    }
}