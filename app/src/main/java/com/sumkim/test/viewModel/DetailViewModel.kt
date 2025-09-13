package com.sumkim.test.viewModel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.sumkim.api.db.FavoriteDB
import com.sumkim.api.db.FavoriteDao
import com.sumkim.api.response.Document
import com.sumkim.test.CommonEvent
import com.sumkim.test.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : BaseViewModel() {

    private val _eventChannel = Channel<CommonEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    private val _document = MutableStateFlow<Document?>(null)
    val document = _document.asStateFlow()

    private val _favoriteDocuments = MutableStateFlow<List<Document>>(listOf())
    val favoriteDocuments = _favoriteDocuments.asStateFlow()

    lateinit var favoriteDao: FavoriteDao

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                favoriteDao = FavoriteDB.getDatabase(context).getFavoriteDao()
                _favoriteDocuments.value = favoriteDao.getFavoriteList()
            }
        }
    }

    fun setDocument(document: Document?) = viewModelScope.launch {
        if (document != null) {
            _document.value = document
        } else {
            _eventChannel.send(CommonEvent.Toast(resId = R.string.error_default))
        }
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
}