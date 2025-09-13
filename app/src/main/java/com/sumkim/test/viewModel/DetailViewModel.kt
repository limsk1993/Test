package com.sumkim.test.viewModel

import androidx.lifecycle.viewModelScope
import com.sumkim.api.response.Document
import com.sumkim.test.CommonEvent
import com.sumkim.test.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(): BaseViewModel() {

    private val _eventChannel = Channel<CommonEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    private val _document = MutableStateFlow<Document?>(null)
    val document = _document.asStateFlow()

    fun setDocument(document: Document?) = viewModelScope.launch {
        if (document != null) {
            _document.value = document
        } else {
            _eventChannel.send(CommonEvent.Toast(resId = R.string.error_default))
        }
    }
}