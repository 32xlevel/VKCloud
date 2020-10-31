package com.example.vkcloud.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkcloud.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel : ViewModel() {
    val progress = SingleLiveEvent<ProgressOptions>()
    val message = SingleLiveEvent<String>()

    fun launchSafety(
        progressOption: ProgressOptions = ProgressOptions(false, false),
        block: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) { progress.value = progressOption }
                block()
                progress.postValue(ProgressOptions(false, false))
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progress.value = ProgressOptions(false, false)
                    message.value = e.message ?: e.localizedMessage ?: e.javaClass.name
                }
                e.printStackTrace()
            }
        }
    }
}

class ProgressOptions(val withProgress: Boolean, val blocked: Boolean)