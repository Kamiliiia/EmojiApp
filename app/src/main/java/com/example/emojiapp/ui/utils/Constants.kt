package com.example.emojiapp.ui.utils

import androidx.compose.material.icons.Icons
// Importy dla ikon kategorii
import androidx.compose.material.icons.filled.Restaurant // Dla kategorii "Jedzenie"
import androidx.compose.material.icons.filled.Bathtub    // Dla kategorii "Łazienka" i podkategorii "Wanna"
import androidx.compose.material.icons.filled.MusicNote  // Dla kategorii "Rozrywka"
import androidx.compose.material.icons.filled.Sos        // Dla kategorii "Inne"

// Importy dla ikon PODKATEGORII (wg Twojego życzenia)
import androidx.compose.material.icons.filled.LunchDining // Dla podkategorii "Jedzenie"
import androidx.compose.material.icons.filled.WineBar     // Dla podkategorii "Picie"
import androidx.compose.material.icons.filled.MovieFilter // Dla podkategorii "Telewizja"
import androidx.compose.material.icons.filled.Forest  // Dla podkategorii "Spacer" - upewnij się, że ta ikona istnieje
// Jeśli nie, alternatywy to Hiking, Forest, DirectionsWalk (autoMirrored)
import androidx.compose.material.icons.filled.Wc          // Dla podkategorii "Toaleta"
// Bathtub już zaimportowane
import androidx.compose.material.icons.filled.HotTub
import androidx.compose.material.icons.filled.LiveTv

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
    const val OTHER_SUBCATEGORY = "Inne" // Nazwa klucza dla kategorii "Inne"

    // Nazwy kategorii (klucze do map)
    const val CATEGORY_JEDZENIE = "Jedzenie"
    const val CATEGORY_LAZIENKA = "Łazienka"
    const val CATEGORY_ROZRYWKA = "Rozrywka"
    // OTHER_SUBCATEGORY jest już zdefiniowane i używane jako nazwa kategorii "Inne"

    val CATEGORIES = listOf(CATEGORY_JEDZENIE, CATEGORY_LAZIENKA, CATEGORY_ROZRYWKA, OTHER_SUBCATEGORY)

    // Nazwy podkategorii (klucze do map i wartości)
    const val SUBCAT_JEDZENIE_FOOD = "Jedzenie" // Podkategoria "Jedzenie" w kategorii "Jedzenie"
    const val SUBCAT_JEDZENIE_DRINK = "Picie"
    const val SUBCAT_LAZIENKA_WC = "Toaleta"
    const val SUBCAT_LAZIENKA_BATH = "Wanna"
    const val SUBCAT_ROZRYWKA_TV = "Telewizja"
    const val SUBCAT_ROZRYWKA_WALK = "Spacer"

    val SUBCATEGORIES: Map<String, List<String>> = mapOf(
        CATEGORY_JEDZENIE to listOf(SUBCAT_JEDZENIE_FOOD, SUBCAT_JEDZENIE_DRINK),
        CATEGORY_LAZIENKA to listOf(SUBCAT_LAZIENKA_WC, SUBCAT_LAZIENKA_BATH),
        CATEGORY_ROZRYWKA to listOf(SUBCAT_ROZRYWKA_TV, SUBCAT_ROZRYWKA_WALK),
        OTHER_SUBCATEGORY to emptyList() // Kategoria "Inne" nie ma predefiniowanych podkategorii wizualnych w ten sposób
    )

    val CATEGORY_ICONS: Map<String, ImageVector> = mapOf(
        CATEGORY_JEDZENIE to Icons.Filled.Restaurant,
        CATEGORY_LAZIENKA to Icons.Filled.Bathtub,
        CATEGORY_ROZRYWKA to Icons.Filled.MusicNote,
        OTHER_SUBCATEGORY to Icons.Filled.Sos
    )

    val SUBCATEGORY_ICONS: Map<String, ImageVector> = mapOf(
        SUBCAT_JEDZENIE_FOOD to Icons.Filled.LunchDining,
        SUBCAT_JEDZENIE_DRINK to Icons.Filled.WineBar,
        SUBCAT_LAZIENKA_WC to Icons.Filled.Wc,
        SUBCAT_LAZIENKA_BATH to Icons.Filled.HotTub, // Ikona dla Wanny
        SUBCAT_ROZRYWKA_TV to Icons.Filled.LiveTv,
        SUBCAT_ROZRYWKA_WALK to Icons.Filled.Forest // Użyj tej ikony. Jeśli jej nie ma w bibliotece, Android Studio podkreśli to na czerwono.
    )
    val CATEGORY_EMOJI = mapOf(
        "Jedzenie" to "\uD83C\uDF54",    //
        "Łazienka" to "\uD83D\uDEBD",    //
        "Rozrywka" to "\uD83C\uDFAC",    //
        OTHER_SUBCATEGORY to "\u2753"   //
    )

    val SUBCATEGORY_EMOJI: Map<String, String> = mapOf(
        // Klucze powinny pasować do stałych używanych w aplikacji, np. Constants.SUBCAT_JEDZENIE_FOOD
        "Jedzenie" to "\uD83C\uDF54",      // 🍔 Hamburger (dla podkategorii "Jedzenie" w kategorii "Jedzenie")
        "Picie" to "\uD83C\uDF79",         // 🍹 Tropikalny drink
        "Toaleta" to "\uD83D\uDEBD",       // 🚽 Symbol WC (często używane emoji dla toalety)
        "Wanna" to "\uD83D\uDEC0",         // 🛀 Osoba w wannie
        "Telewizja" to "\uD83D\uDCFA",     // 📺 Telewizor
        "Spacer" to "\uD83D\uDEB6")

}