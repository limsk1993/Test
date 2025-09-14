package com.sumkim.test.viewModel

import android.content.Context
import androidx.lifecycle.viewModelScope
import androidx.sqlite.db.SimpleSQLiteQuery
import com.sumkim.api.response.Document
import com.sumkim.test.Sort
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
): BaseViewModel(context) {

    private val _query: MutableStateFlow<String> = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _sort: MutableStateFlow<String?> = MutableStateFlow(null)
    val sort: StateFlow<String?> = _sort.asStateFlow()

    private val _documents = MutableStateFlow<List<Document>>(listOf())
    val documents = _documents.asStateFlow()

    init {
        viewModelScope.launch {
            getFavoriteQuerySortDocuments()
        }
    }

    suspend fun getFavoriteQuerySortDocuments() {
        withContext(Dispatchers.IO) {
            val sort = when (sort.value) {
                Sort.ASC.value -> "ORDER BY title ASC"
                Sort.DESC.value -> "ORDER BY title DESC"
                Sort.LOWEST_PRICE.value -> "ORDER BY price ASC"
                Sort.HIGHEST_PRICE.value -> "ORDER BY price DESC"
                else -> ""
            }
            val query = SimpleSQLiteQuery(
                "SELECT * FROM favorite_table WHERE title LIKE \"%${query.value}%\" $sort",
            )
            _documents.value = favoriteDao.getFavoriteQuerySortFilterList(query)
        }
    }

    suspend fun favoriteSearch(searchQuery: String) {
        if (query.value == searchQuery) return
        _query.value = searchQuery
        getFavoriteQuerySortDocuments()
    }

    suspend fun setFavoriteSort(favoriteSort: String) {
        if (sort.value == favoriteSort) return
        _sort.value = favoriteSort
        getFavoriteQuerySortDocuments()
    }

    fun getFavoriteAfterToggle(document: Document) = viewModelScope.launch {
        toggleFavorite(document)
        getFavoriteQuerySortDocuments()
    }
}