package com.sumkim.test.route

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sumkim.api.request.Sort
import com.sumkim.api.response.Document
import com.sumkim.test.R
import com.sumkim.test.Route
import com.sumkim.test.RouteProvider
import com.sumkim.test.getLocalDate
import com.sumkim.test.moveToDetail
import com.sumkim.test.ui.theme.TestTheme
import com.sumkim.test.viewModel.MainViewModel
import com.sumkim.view.component.BottomCallBackLazyColumn
import com.sumkim.view.component.CustomPicker
import com.sumkim.view.component.CustomSearchBar
import com.sumkim.view.component.CustomText
import com.sumkim.view.component.DocumentCard
import com.sumkim.view.component.PullToRefreshBox

@Composable
fun SearchRoute(
    vm: MainViewModel = hiltViewModel()
) {
    val nav = Route.nav
    val isLoading by vm.isLoading.collectAsStateWithLifecycle()
    val documents by vm.documents.collectAsStateWithLifecycle()
    val favoriteDocuments by vm.favoriteDocuments.collectAsStateWithLifecycle()
    val sort by vm.sort.collectAsStateWithLifecycle()

    val backStackEntry = remember { nav.currentBackStackEntry }
    LaunchedEffect(backStackEntry) {
        vm.getFavoriteDocuments()
    }

    SearchScreen(
        modifier = Modifier.fillMaxSize(),
        documents = documents,
        onSearch = vm::querySearch,
        favoriteDocuments = favoriteDocuments,
        onFavoriteClick = vm::toggleFavorite,
        atBottom = vm::getV3SearchBook,
        isRefreshing = isLoading,
        onRefresh = vm::refresh,
        sort = sort,
        onSort = vm::setSort
    )
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    documents: List<Document>,
    onSearch: (String) -> Unit,
    favoriteDocuments: List<Document>,
    onFavoriteClick: (Document) -> Unit,
    atBottom: () -> Unit,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    sort: String,
    onSort: (String) -> Unit,
) {
    val nav = Route.nav
    Column(
        modifier = modifier
    ) {
        CustomSearchBar(
            onSearch = onSearch
        )
        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            CustomText(
                modifier = Modifier.align(Alignment.CenterStart),
                text = if (sort == Sort.ACCURACY.value) "정확도순" else "발간일순",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                CustomPicker(
                    title = "정렬",
                    resId = R.drawable.ic_sort,
                    items = listOf(Sort.ACCURACY.value, Sort.LATEST.value),
                    onItemSelected = {
                        onSort(it)
                    }
                )
            }
        }
        Spacer(Modifier.height(8.dp))

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh
        ) {
            BottomCallBackLazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.LightGray),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp),
                atBottom = {
                    atBottom.invoke()
                }
            ) {
                items(documents) { document ->
                    DocumentCard(
                        title = document.title,
                        thumbnail = document.thumbnail,
                        publisher = document.publisher,
                        authors = document.authors,
                        price = document.price,
                        salePrice = document.salePrice,
                        datetime = document.datetime?.getLocalDate(),
                        onDocumentClick = {
                            nav.moveToDetail(document)
                        },
                        isFavorite = favoriteDocuments.contains(document),
                        onFavoriteClick = { onFavoriteClick(document) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchPreview() {
    TestTheme {
        RouteProvider {
            SearchRoute()
        }
    }
}