package com.example.vkcloud.viewmodel

import androidx.lifecycle.MutableLiveData
import com.vk.api.sdk.VK
import com.vk.sdk.api.GsonHolder
import com.vk.sdk.api.photos.dto.PhotosPhoto
import com.vk.sdk.api.photos.dto.PhotosPhotoUploadResponse
import com.vk.sdk.api.photos.methods.PhotosGet
import com.vk.sdk.api.photos.methods.PhotosGetUploadServer
import com.vk.sdk.api.photos.methods.PhotosSave
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream

class AlbumViewModel : BaseViewModel() {
    private enum class ClosedAlbumType(val id: Int) {
        PROFILE(-6), WALL(-7), SAVED(-15);

        companion object {
            fun getBy(id: Int) = values().find { it.id == id }?.name?.toLowerCase() ?: id.toString()
        }
    }

    val photos = MutableLiveData<List<PhotosPhoto>>(listOf())

    fun getPhotos(albumId: Int) {
        launchSafety(progressOption = ProgressOptions(withProgress = true, blocked = false)) {
            val id = ClosedAlbumType.getBy(albumId)

            val photosR = VK.executeSync(PhotosGet(ownerId = VK.getUserId(), albumId = id, count = 1000))
            photos.postValue(photosR.items)
        }
    }

    fun uploadPhoto(openInputStream: InputStream?, albumId: Int) {
        openInputStream ?: return

        launchSafety(progressOption = ProgressOptions(withProgress = true, blocked = true)) {
            //1
            val part = getPart(openInputStream)

            //2
            val upload = uploadPhoto(albumId, part)

            //3
            val uploadedPhoto = VK.executeSync(
                PhotosSave(
                    albumId = albumId,
                    server = upload.server,
                    photosList = upload.photosList,
                    hash = upload.hash
                )
            ).first()

            photos.postValue(photos.value!!.toMutableList().also { it.add(uploadedPhoto) })
        }
    }

    private fun getPart(stream: InputStream): MultipartBody.Part {
        val byteArray = stream.use { input -> input.readBytes() }
        val reqFile: RequestBody = byteArray.toRequestBody("image/jpeg".toMediaType())
        return MultipartBody.Part.createFormData("file1", "name.jpg", reqFile)
    }

    private fun uploadPhoto(albumId: Int, part: MultipartBody.Part): PhotosPhotoUploadResponse {
        val photoUpload = VK.executeSync(PhotosGetUploadServer(albumId = albumId))
        val request = Request.Builder()
            .url(photoUpload.uploadUrl)
            .post(
                MultipartBody.Builder()
                    .addPart(part)
                    .setType(MultipartBody.FORM)
                    .build()
            )
            .build()

        val response = OkHttpClient().newCall(request).execute()
        val json = response.body!!.string()
        return GsonHolder.gson.fromJson(json, PhotosPhotoUploadResponse::class.java)
    }
}