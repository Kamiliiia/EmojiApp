package com.example.emojiapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.emojiapp.ui.screens.ConfirmationScreen
import com.example.emojiapp.ui.screens.MainScreen
import com.example.emojiapp.ui.screens.MessageSentScreen
import com.example.emojiapp.ui.screens.SettingsScreen
import com.example.emojiapp.ui.screens.StartScreen
import com.example.emojiapp.ui.screens.SubcategoryScreen
import com.example.emojiapp.ui.utils.Constants.CATEGORY_ARGUMENT
import com.example.emojiapp.ui.utils.Constants.CONFIRMATION_ROUTE
import com.example.emojiapp.ui.utils.Constants.MAIN_ROUTE
import com.example.emojiapp.ui.utils.Constants.MESSAGE_SENT_ROUTE
import com.example.emojiapp.ui.utils.Constants.SETTINGS_ROUTE
import com.example.emojiapp.ui.utils.Constants.START_ROUTE
import com.example.emojiapp.ui.utils.Constants.SUBCATEGORY_ARGUMENT
import com.example.emojiapp.ui.utils.Constants.SUBCATEGORIES_ROUTE
import com.example.emojiapp.BLEManager

@Composable
fun AppNavigation(bleManager: BLEManager) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = START_ROUTE) {
        composable(START_ROUTE) { StartScreen(navController) }
        composable(MAIN_ROUTE) { MainScreen(navController) }
        composable(
            route = "$SUBCATEGORIES_ROUTE{$CATEGORY_ARGUMENT}",
            arguments = listOf(navArgument(CATEGORY_ARGUMENT) { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString(CATEGORY_ARGUMENT) ?: ""
            SubcategoryScreen(category = category, navController = navController)
        }
        composable(
            route = "$CONFIRMATION_ROUTE{$SUBCATEGORY_ARGUMENT}",
            arguments = listOf(navArgument(SUBCATEGORY_ARGUMENT) { type = NavType.StringType })
        ) { backStackEntry ->
            val subcategory = backStackEntry.arguments?.getString(SUBCATEGORY_ARGUMENT) ?: ""
            // Przekaż bleManager do ConfirmationScreen
            ConfirmationScreen(
                subcategory = subcategory,
                navController = navController,
                bleManager = bleManager // Dodano
            )
        }
        composable(MESSAGE_SENT_ROUTE) { MessageSentScreen(navController) }
        composable(SETTINGS_ROUTE) { SettingsScreen(navController) }
    }
}