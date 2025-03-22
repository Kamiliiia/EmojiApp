package com.example.emojiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import com.example.emojiapp.ui.theme.EmojiAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmojiAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "start") {
                        composable("start") { StartScreen(navController) }
                        composable("main") { MainScreen(navController) }
                        composable("subcategories/{category}") { backStackEntry ->
                            val category = backStackEntry.arguments?.getString("category") ?: ""
                            SubcategoryScreen(category = category, navController = navController)
                        }
                        composable("confirmation/{subcategory}") { backStackEntry ->
                            val subcategory = backStackEntry.arguments?.getString("subcategory") ?: ""
                            ConfirmationScreen(subcategory = subcategory, navController = navController)
                        }
                        composable("messageSent") { MessageSentScreen() }
                        composable("settings") { SettingsScreen(navController) }
                    }
                }
            }
        }
    }
}


@Composable
fun StartScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { navController.navigate("main") },
        contentAlignment = Alignment.Center
    ) {
        // Ustawienia (ikona)
        IconButton(
            onClick = { navController.navigate("settings") },
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val pagerState = rememberPagerState(pageCount = { 4 })
    val categories = listOf("Jedzenie", "Łazienka", "Rozrywka", "Inne")

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(state = pagerState) { page ->
                CategoryScreen(category = categories[page], navController = navController)
            }
        }
    }
}

@Composable
fun CategoryScreen(category: String, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                if (category != "Inne") {
                    navController.navigate("subcategories/$category")
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubcategoryScreen(category: String, navController: NavHostController) {
    val subcategories = when (category) {
        "Jedzenie" -> listOf("Jedzenie", "Picie")
        "Łazienka" -> listOf("Toaleta", "Wanna")
        "Rozrywka" -> listOf("Telewizja", "Spacer")
        else -> emptyList()
    }
    val pagerState = rememberPagerState(pageCount = { subcategories.size })

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Podkategorie dla: $category",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
            HorizontalPager(state = pagerState) { page ->
                SubcategoryItem(subcategory = subcategories[page], navController = navController)
            }
        }
    }
}

@Composable
fun SubcategoryItem(subcategory: String, navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                navController.navigate("confirmation/$subcategory")
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = subcategory,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun ConfirmationScreen(subcategory: String, navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Czy na pewno chcesz wysłać komunikat \"$subcategory\"?",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = { navController.navigate("messageSent") }) {
                Text("Tak")
            }
            Button(onClick = { navController.popBackStack() }) {
                Text("Nie")
            }
        }
    }
}

@Composable
fun MessageSentScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Wiadomość wysłana!",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    EmojiAppTheme {
        val navController = rememberNavController()
        MainScreen(navController)
    }
}

@Composable
fun SettingsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Ustawienia",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
        Button(onClick = { navController.popBackStack() }) {
            Text("Wróć")
        }
    }
}