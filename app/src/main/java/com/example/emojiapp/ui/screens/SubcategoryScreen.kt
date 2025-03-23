package com.example.emojiapp.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.emojiapp.ui.components.SubcategoryItem
import com.example.emojiapp.ui.utils.Constants

/**
 * Ekran z podkategoriami.
 * Wyświetla podkategorie dla wybranej kategorii w formie przewijanej galerii.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubcategoryScreen(category: String, navController: NavHostController) {
    val subcategories = Constants.SUBCATEGORIES[category] ?: emptyList()
    Log.d("SubcategoryScreen", "Subcategories for $category: $subcategories")
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