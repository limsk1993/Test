package com.sumkim.test.viewModel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.sumkim.api.repository.ApiRepository
import com.sumkim.api.request.GetV3SearchBookRequest
import com.sumkim.api.response.Document
import com.sumkim.api.call.onError
import com.sumkim.api.call.onException
import com.sumkim.api.call.onSuccess
import com.sumkim.api.db.FavoriteDB
import com.sumkim.api.db.FavoriteDao
import com.sumkim.test.CommonEvent
import com.sumkim.test.Filter
import com.sumkim.test.R
import com.sumkim.test.Sort
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ar: ApiRepository
): BaseViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _eventChannel = Channel<CommonEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    private val _query: MutableStateFlow<String> = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _searchSort: MutableStateFlow<String> = MutableStateFlow(Sort.ACCURACY.value)
    val searchSort: StateFlow<String> = _searchSort.asStateFlow()

    private val _page: MutableStateFlow<Int> = MutableStateFlow(1)
    val page: StateFlow<Int> = _page.asStateFlow()

    private val _documents = MutableStateFlow<List<Document>>(listOf())
    val documents = _documents.asStateFlow()

    private val _favoriteDocuments = MutableStateFlow<List<Document>>(listOf())
    val favoriteDocuments = _favoriteDocuments.asStateFlow()

    private val _favoriteQuery: MutableStateFlow<String> = MutableStateFlow("")
    val favoriteQuery: StateFlow<String> = _favoriteQuery.asStateFlow()

    private val _favoriteSort: MutableStateFlow<String> = MutableStateFlow(Sort.ASC.value)
    val favoriteSort: StateFlow<String> = _favoriteSort.asStateFlow()

    private val _favoriteFilter: MutableStateFlow<String> = MutableStateFlow(Filter.LOWEST_PRICE.value)
    val favoriteFilter: StateFlow<String> = _favoriteFilter.asStateFlow()

    lateinit var favoriteDao: FavoriteDao

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favoriteDao = FavoriteDB.getDatabase(context).getFavoriteDao()
                getFavoriteDocuments()
            }
        }
    }

    fun getFavoriteDocuments() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            _favoriteDocuments.value = favoriteDao.getFavoriteList()
        }
    }

    fun querySearch(query: String) {
        _query.value = query
        _page.value = 1
        _documents.value = listOf()
        getV3SearchBook()
    }

    fun setSearchSort(sort: String) {
        if (searchSort.value == sort) return
        _searchSort.value = sort
        _page.value = 1
        _documents.value = listOf()
        getV3SearchBook()
    }

    fun getV3SearchBook() = viewModelScope.launch {
        try {
            _isLoading.value = true

            ar.getV3SearchBook(
                GetV3SearchBookRequest(
                    query = query.value,
                    sort = searchSort.value,
                    page = page.value,
                    size = 20
                )
            ).onSuccess {
                _page.value = page.value + 1

                val tempDocuments = mutableListOf<Document>()
                tempDocuments.addAll(documents.value)
                tempDocuments.addAll(it.documents ?: listOf())
                _documents.value = tempDocuments
                _isLoading.value = false
            }.onError {
                _eventChannel.send(CommonEvent.Toast(resId = R.string.error_default))
                _isLoading.value = false
            }.onException {
                _eventChannel.send(CommonEvent.Toast(resId = R.string.error_default))
                _isLoading.value = false
            }
        } catch (e: Exception) {
            _eventChannel.send(CommonEvent.Toast(resId = R.string.error_default))
            _isLoading.value = false
        }
    }

    fun refresh() = viewModelScope.launch {
        _searchSort.value = Sort.ACCURACY.value
        _page.value = 1
        _documents.value = listOf()
        getV3SearchBook()
    }

    fun toggleFavorite(document: Document) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            try {
                val favoriteDocument = favoriteDao.getFavorite(document.isbn)
                if (favoriteDocument.isEmpty()) {
                    favoriteDao.insertFavorite(document)
                } else {
                    favoriteDao.deleteFavorite(document)
                }
            } catch (e: Exception) {
                _eventChannel.send(CommonEvent.Toast(resId = R.string.error_default))
            } finally {
                _favoriteDocuments.value = favoriteDao.getFavoriteList()
            }
        }
    }

    fun favoriteSearch(query: String) {
        _favoriteQuery.value = query
    }

    fun setFavoriteSort(sort: String) {
        if (favoriteSort.value == sort) return
        _favoriteSort.value = sort
    }

    fun setFavoriteFilter(filter: String) {
        if (favoriteFilter.value == filter) return
        _favoriteFilter.value = filter
    }
}