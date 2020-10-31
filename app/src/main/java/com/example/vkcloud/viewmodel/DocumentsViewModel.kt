package com.example.vkcloud.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vkapi.docs.methods.DocsGet
import com.example.vkapi.docs.methods.DocsUpload
import com.example.vkcloud.utils.SingleLiveEvent
import com.vk.api.sdk.VK
import com.vk.sdk.api.docs.dto.DocsDoc
import com.vk.sdk.api.docs.methods.DocsDelete
import com.vk.sdk.api.docs.methods.DocsEdit
import com.vk.sdk.api.docs.methods.DocsGetUploadServer
import com.vk.sdk.api.docs.methods.DocsSave
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class DocumentsViewModel : BaseViewModel() {
    val docs = MutableLiveData<List<DocsDoc>>(listOf())

    val isSearch = MutableLiveData(false)
    val searchQuery = MutableLiveData("")

    fun isSearch() = isSearch.value!!
    fun searchQuery() = searchQuery.value!!
    fun docs() = docs.value!!

    fun loadDocuments() {
        launchSafety(progressOption = ProgressOptions(withProgress = true, blocked = false)) {
            val documentResponse = VK.executeSync(DocsGet(ownerId = VK.getUserId(), returnTags = true))
            docs.postValue(documentResponse.response.items)
        }
    }

    fun addDocument(file: File) {
        launchSafety(progressOption = ProgressOptions(withProgress = true, blocked = true)) {
            val server = VK.executeSync(DocsGetUploadServer())
            val upload = DocsUpload().upload(file = file, server = server.uploadUrl)
            val save = VK.executeSync(DocsSave(file = upload.file!!, title = file.name, returnTags = true))

            docs.postValue(docs().toMutableList().also { it.add(2, save.doc!!) })
        }
    }

    fun renameDocument(id: Int, title: String) {
        launchSafety(
            method = { VK.executeSync(DocsEdit(ownerId = VK.getUserId(), docId = id, title = title)) },
            onSuccess = { renameDocumentState(id, title) },
            onFailure = { message.value = "Не удалось переименовать документ, попробуйте снова" }
        )
    }

    private fun renameDocumentState(id: Int, title: String) {
        val list = docs().toMutableList()

        val finded = list.find { it.id == id }!!
        val index = list.indexOf(finded)
        val copy = finded.copy(title = title)

        list[index] = copy

        docs.postValue(list)
    }

    fun deleteDocument(id: Int) {
        launchSafety(
            method = { VK.executeSync(DocsDelete(ownerId = VK.getUserId(), docId = id)) },
            onSuccess = { docs.postValue(docs().toMutableList().also { it.removeAll { it.id == id } }) },
            onFailure = { message.value = "Не удалось удалить документ, попробуйте снова" }
        )
    }

    fun handleSearchMode(search: Boolean) {
        isSearch.value = search
    }

    fun handleSearch(query: String?) {
        if (query == null) return
        searchQuery.value = query
    }

    private fun launchSafety(method: () -> Unit, onSuccess: () -> Unit, onFailure: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                method()
                onSuccess()
            } catch (e: NullPointerException) {
                onSuccess()
            } catch (e: Exception) {
                onFailure()
                e.printStackTrace()
            }
        }
    }
}