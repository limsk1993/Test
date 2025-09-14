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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sumkim.api.response.Document
import com.sumkim.test.R
import com.sumkim.test.Route
import com.sumkim.test.RouteProvider
import com.sumkim.test.Sort
import com.sumkim.test.getLocalDate
import com.sumkim.test.moveToDetail
import com.sumkim.test.ui.theme.TestTheme
import com.sumkim.test.viewModel.FavoriteViewModel
import com.sumkim.view.component.CustomPicker
import com.sumkim.view.component.CustomSearchBar
import com.sumkim.view.component.CustomText
import com.sumkim.view.component.DocumentCard
import kotlinx.coroutines.launch


@Composable
fun FavoriteRoute(
    vm: FavoriteViewModel = hiltViewModel()
) {
    val nav = Route.nav
    val documents by vm.documents.collectAsStateWithLifecycle()
    val favoriteDocuments by vm.favoriteDocuments.collectAsStateWithLifecycle()
    val sort by vm.sort.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    val backStackEntry = remember { nav.currentBackStackEntry }
    LaunchedEffect(backStackEntry) {
        vm.getFavoriteDocuments()
        vm.getFavoriteQuerySortDocuments()
    }

    FavoriteScreen(
        modifier = Modifier.fillMaxSize(),
        documents = documents,
        favoriteDocuments = favoriteDocuments,
        onSearch = { onSearch -> coroutineScope.launch { vm.favoriteSearch(onSearch) } },
        onFavoriteClick = vm::getFavoriteAfterToggle,
        sort = sort,
        onSort = { onSort -> coroutineScope.launch { vm.setFavoriteSort(onSort) } },
    )
}

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit,
    documents: List<Document>,
    favoriteDocuments: List<Document>,
    onFavoriteClick: (Document) -> Unit,
    sort: String?,
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
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                CustomText(
                    text = when (sort) {
                        Sort.ASC.value -> "오름차순(제목)"
                        Sort.DESC.value -> "내림차순(제목)"
                        Sort.LOWEST_PRICE.value -> "낮은가격순"
                        Sort.HIGHEST_PRICE.value -> "높은가격순"
                        else -> ""
                    },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                CustomPicker(
                    title = "필터",
                    resId = R.drawable.ic_filter,
                    items = listOf(
                        Pair("오름차순(제목)", Sort.ASC.value),
                        Pair("내림차순(제목)", Sort.DESC.value)
                    ),
                    onItemSelected = {
                        onSort(it)
                    }
                )
                Spacer(Modifier.width(4.dp))
                CustomPicker(
                    title = "정렬",
                    resId = R.drawable.ic_sort,
                    items = listOf(
                        Pair("낮은가격순", Sort.LOWEST_PRICE.value),
                        Pair("높은가격순", Sort.HIGHEST_PRICE.value)
                    ),
                    onItemSelected = {
                        onSort(it)
                    }
                )
            }
        }
        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Color.LightGray),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp),
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

@Preview(showBackground = true)
@Composable
fun FavoritePreview() {
    TestTheme {
        RouteProvider {
            FavoriteRoute()
        }
    }
}