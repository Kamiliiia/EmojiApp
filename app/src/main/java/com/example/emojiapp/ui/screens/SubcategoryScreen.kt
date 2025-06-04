package com.example.emojiapp.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size // Potrzebny import
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme // Dodaj, jeśli używasz MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text // Dodaj, jeśli chcesz wyświetlać tekst, np. "Brak podkategorii"
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Możesz chcieć zastąpić kolorami z motywu
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.emojiapp.ui.components.SubcategoryItem // Upewnij się, że ten import jest poprawny
import com.example.emojiapp.ui.utils.Constants

/**
 * Ekran z podkategoriami.
 * Wyświetla podkategorie dla wybranej kategorii w formie przewijanej galerii,
 * gdzie każda podkategoria ma dużą ikonę i podpis.
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SubcategoryScreen(category: String, navController: NavHostController) {
    val subcategories = Constants.SUBCATEGORIES[category] ?: emptyList()
    Log.d("SubcategoryScreen", "Category: $category, Subcategories: $subcategories")

    // PagerState powinien być tworzony warunkowo lub obsłużyć pageCount = 0,
    // jeśli lista podkategorii może być pusta.
    // Dla uproszczenia, jeśli subcategories.size jest 0, HorizontalPager nie powinien być wyświetlany.
    val pagerState = rememberPagerState(pageCount = { subcategories.size })

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(category) }, // Wyświetl nazwę kategorii w TopAppBar
                navigationIcon = {
                    IconButton(onClick = {
                        // Popraw nawigację, aby wracać do MAIN_ROUTE i czyścić stos
                        navController.navigate(Constants.MAIN_ROUTE) {
                            popUpTo(Constants.MAIN_ROUTE) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Wróć do kategorii",
                            // Rozważ użycie koloru z motywu zamiast Color.Black
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent // Lub kolor z motywu
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), // Użyj innerPadding z Scaffold
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (subcategories.isNotEmpty()) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize() // Pager powinien zająć dostępną przestrzeń
                ) { page ->
                    // Upewnij się, że `page` jest w zakresie listy podkategorii
                    if (page < subcategories.size) {
                        SubcategoryItem(
                            subcategoryName = subcategories[page],
                            navController = navController
                        )
                    } else {
                        // Ten blok nie powinien być osiągnięty, jeśli pageCount jest poprawnie ustawiony
                        Log.w("SubcategoryScreen", "Pager requested page $page which is out of bounds for subcategories size ${subcategories.size}")
                    }
                }
            } else {
                // Co wyświetlić, jeśli nie ma podkategorii dla danej kategorii
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val categoryIcon = Constants.CATEGORY_ICONS[category]
                    if (categoryIcon != null) {
                        Icon(
                            imageVector = categoryIcon,
                            contentDescription = "Kategoria: $category",
                            modifier = Modifier
                                .size(64.dp)
                                .padding(bottom = 16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "Brak dostępnych podkategorii dla \"$category\"",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}