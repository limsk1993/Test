package com.sumkim.test.route

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sumkim.api.response.Document
import com.sumkim.test.Route
import com.sumkim.test.RouteProvider
import com.sumkim.test.moveToDetail
import com.sumkim.test.ui.theme.TestTheme
import com.sumkim.test.viewModel.MainViewModel
import com.sumkim.view.component.CustomSearchBar
import com.sumkim.view.component.ItemCard


@Composable
fun FavoriteRoute(
    vm: MainViewModel = hiltViewModel()
) {
    val favoriteItems by vm.favoriteItems.collectAsStateWithLifecycle()
    FavoriteScreen(
        modifier = Modifier.fillMaxSize(),
        favoriteItems = favoriteItems,
        onSearch = {},
        onFavoriteClick = vm::toggleFavorite
    )
}

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    favoriteItems: List<Document>,
    onFavoriteClick: (Document) -> Unit,
) {
    val nav = Route.nav
    Column(
        modifier = modifier
    ) {
        CustomSearchBar(
            onSearch = onSearch
        )
        Spacer(Modifier.height(8.dp))
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Color.LightGray),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp),
        ) {
            items(favoriteItems) { item ->
                ItemCard(
                    title = item.title,
                    thumbnail = item.thumbnail,
                    publisher = item.publisher,
                    authors = item.authors,
                    price = item.price,
                    salePrice = item.salePrice,
                    onItemClick = {
                        nav.moveToDetail(item.isbn)
                    },
                    isFavorite = favoriteItems.contains(item),
                    onFavoriteClick = { onFavoriteClick(item) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritePreview() {
    TestTheme {
        RouteProvider {
            FavoriteRoute()
        }
    }
}