package com.example.emojiapp.ui.utils

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
    val CATEGORIES = listOf("Jedzenie", "Łazienka", "Rozrywka", "Inne")
    val SUBCATEGORIES = mapOf(
        "Jedzenie" to listOf("Jedzenie", "Picie"),
        "Łazienka" to listOf("Toaleta", "Wanna"),
        "Rozrywka" to listOf("Telewizja", "Spacer"),
        "Inne" to emptyList()
    )
}