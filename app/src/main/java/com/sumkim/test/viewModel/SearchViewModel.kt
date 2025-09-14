package com.sumkim.test.viewModel

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.sumkim.api.call.onError
import com.sumkim.api.call.onException
import com.sumkim.api.call.onSuccess
import com.sumkim.api.repository.ApiRepository
import com.sumkim.api.request.GetV3SearchBookRequest
import com.sumkim.api.response.Document
import com.sumkim.test.CommonEvent
import com.sumkim.test.R
import com.sumkim.test.Sort
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ar: ApiRepository
) : BaseViewModel(context) {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _query: MutableStateFlow<String> = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _sort: MutableStateFlow<String> = MutableStateFlow(Sort.ACCURACY.value)
    val sort: StateFlow<String> = _sort.asStateFlow()

    private val _page: MutableStateFlow<Int> = MutableStateFlow(1)
    val page: StateFlow<Int> = _page.asStateFlow()

    private val _documents = MutableStateFlow<List<Document>>(listOf())
    val documents = _documents.asStateFlow()

    fun querySearch(query: String) {
        _query.value = query
        _page.value = 1
        _documents.value = listOf()
        getV3SearchBook()
    }

    fun refresh() = viewModelScope.launch {
        _sort.value = Sort.ACCURACY.value
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
                    sort = sort.value,
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

    fun setSearchSort(searchSort: String) {
        if (sort.value == searchSort) return
        _sort.value = searchSort
        _page.value = 1
        _documents.value = listOf()
        getV3SearchBook()
    }
}