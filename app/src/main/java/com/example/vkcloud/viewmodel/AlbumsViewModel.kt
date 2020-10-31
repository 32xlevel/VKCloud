package com.example.vkcloud.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vk.api.sdk.VK
import com.vk.sdk.api.photos.dto.PhotosPhotoAlbumFull
import com.vk.sdk.api.photos.methods.PhotosCreateAlbum
import com.vk.sdk.api.photos.methods.PhotosDeleteAlbum
import com.vk.sdk.api.photos.methods.PhotosGetAlbums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlbumsViewModel : BaseViewModel() {

    val albums = MutableLiveData<List<PhotosPhotoAlbumFull>>(listOf())
    val albumsMode = MutableLiveData(AlbumsMode.WATCH)

    fun loadAlbums() {
        launchSafety(ProgressOptions(withProgress = true, blocked = false)) {
            val alb = VK.executeSync(
                PhotosGetAlbums(
                    ownerId = VK.getUserId(),
                    needSystem = true,
                    needCovers = true,
                    photoSizes = true
                )
            )
            albums.postValue(alb.items)
        }
    }

    fun removeAlbum(album: PhotosPhotoAlbumFull) {
        launchSafety {
            try {
                VK.executeSync(PhotosDeleteAlbum(album.id))
                albums.postValue(albums.value!!.toMutableList().also { it.remove(album) })
            } catch (e: NullPointerException) {
                albums.postValue(albums.value!!.toMutableList().also { it.remove(album) })
            }
        }

    }

    fun createAlbum(title: String) {
        launchSafety {
            var album = VK.executeSync(PhotosCreateAlbum(title = title))
            album = VK.executeSync(
                PhotosGetAlbums(
                    ownerId = VK.getUserId(),
                    albumIds = listOf(album.id),
                    needCovers = true
                )
            ).items.first()
            albums.postValue(albums.value!!.toMutableList().also { it.add(album) })
        }
    }

    fun setAlbumMode(mode: AlbumsMode) {
        albumsMode.value = mode
        albums.value = albums.value!!.toMutableList()
    }
}

enum class AlbumsMode {
    WATCH,
    EDIT
}