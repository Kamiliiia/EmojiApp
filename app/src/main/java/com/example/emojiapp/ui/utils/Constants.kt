package com.example.emojiapp.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Sos
import androidx.compose.ui.graphics.vector.ImageVector

object Constants {
    const val START_ROUTE = "start"
    const val MAIN_ROUTE = "main"
    const val SETTINGS_ROUTE = "settings"
    const val SUBCATEGORIES_ROUTE = "subcategories/"
    const val CATEGORY_ARGUMENT = "category"
    const val CONFIRMATION_ROUTE = "confirmation/"
    const val SUBCATEGORY_ARGUMENT = "subcategory"
    const val MESSAGE_SENT_ROUTE = "messageSent"
    const val OTHER_SUBCATEGORY = "Inne"
    val CATEGORIES = listOf("Jedzenie", "Łazienka", "Rozrywka", OTHER_SUBCATEGORY)
    val SUBCATEGORIES = mapOf(
        "Jedzenie" to listOf("Jedzenie", "Picie"),
        "Łazienka" to listOf("Toaleta", "Wanna"),
        "Rozrywka" to listOf("Telewizja", "Spacer"),
        OTHER_SUBCATEGORY to emptyList()
    )
    val CATEGORY_ICONS: Map<String, ImageVector> = mapOf(
        "Jedzenie" to Icons.Filled.Restaurant,
        "Łazienka" to Icons.Filled.Bathtub,
        "Rozrywka" to Icons.Filled.MusicNote,
        OTHER_SUBCATEGORY to Icons.Filled.Sos // Dodano ikonkę SOS
    )
}