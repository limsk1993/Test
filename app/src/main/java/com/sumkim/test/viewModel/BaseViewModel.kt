package com.sumkim.test.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sumkim.api.db.FavoriteDB
import com.sumkim.api.db.FavoriteDao
import com.sumkim.api.response.Document
import com.sumkim.test.CommonEvent
import com.sumkim.test.R
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel(
    @ApplicationContext private val context: Context,
): ViewModel() {
    private val isInit = MutableStateFlow(false)
    fun ensureInit(): Boolean = !isInit.compareAndSet(expect = false, update = true)

    internal val _eventChannel = Channel<CommonEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    private val _favoriteDocuments = MutableStateFlow<List<Document>>(listOf())
    val favoriteDocuments = _favoriteDocuments.asStateFlow()

    lateinit var favoriteDao: FavoriteDao

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favoriteDao = FavoriteDB.getDatabase(context).getFavoriteDao()
                getFavoriteDocuments()
            }
        }
    }

    suspend fun getFavoriteDocuments() {
        withContext(Dispatchers.IO) {
            _favoriteDocuments.value = favoriteDao.getFavoriteList()
        }
    }

    suspend fun toggleFavorite(document: Document) {
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
}