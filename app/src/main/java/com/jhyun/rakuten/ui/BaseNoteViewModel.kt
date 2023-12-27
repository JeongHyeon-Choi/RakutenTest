package com.jhyun.rakuten.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.jhyun.rakuten.util.SingleLiveEvent

open class BaseNoteViewModel : ViewModel() {

    private val _toastEvent = SingleLiveEvent<String>()
    val toastEvent: LiveData<String> = _toastEvent

    protected fun sendToastMessage(message: String) {
        _toastEvent.value = message
    }
}