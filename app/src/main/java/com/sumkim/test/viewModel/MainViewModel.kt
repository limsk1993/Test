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
import com.sumkim.test.R
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

    private val _page: MutableStateFlow<Int> = MutableStateFlow(1)
    val page: StateFlow<Int> = _page.asStateFlow()

    private val _items = MutableStateFlow<List<Document>>(listOf())
    val items = _items.asStateFlow()

    private val _favoriteItems = MutableStateFlow<List<Document>>(listOf())
    val favoriteItems = _favoriteItems.asStateFlow()

    lateinit var favoriteDao: FavoriteDao

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favoriteDao = FavoriteDB.getDatabase(context).getFavoriteDao()
                _favoriteItems.value = favoriteDao.getFavoriteList()
            }
        }
    }

    fun querySearch(query: String) {
        _query.value = query
        _page.value = 1
        _items.value = listOf()
        getV3SearchBook()
    }

    fun getV3SearchBook() = viewModelScope.launch {
        try {
            _isLoading.value = true

            ar.getV3SearchBook(
                GetV3SearchBookRequest(
                    query = query.value,
                    page = page.value,
                    size = 20
                )
            ).onSuccess {
                _page.value = page.value + 1

                val tempItems = mutableListOf<Document>()
                tempItems.addAll(items.value)
                tempItems.addAll(it.documents ?: listOf())
                _items.value = tempItems
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
        _page.value = 1
        _items.value = listOf()
        getV3SearchBook()
    }

    fun toggleFavorite(item: Document) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            try {
                val favoriteItem = favoriteDao.getFavorite(item.isbn)
                if (favoriteItem.isEmpty()) {
                    favoriteDao.insertFavorite(item)
                } else {
                    favoriteDao.deleteFavorite(item)
                }
            } catch (e: Exception) {
                _eventChannel.send(CommonEvent.Toast(resId = R.string.error_default))
            } finally {
                _favoriteItems.value = favoriteDao.getFavoriteList()
            }
        }
    }

    fun selectedSortItems(targetIsbn: String?): Document? {
        val items = items.value + favoriteItems.value
        val targetItem = items.find { it.isbn == targetIsbn }
        return targetItem
    }
}