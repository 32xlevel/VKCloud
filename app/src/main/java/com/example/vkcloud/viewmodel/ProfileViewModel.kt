package com.example.vkcloud.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.api.sdk.VK
import com.vk.sdk.api.users.dto.UsersFields
import com.vk.sdk.api.users.dto.UsersUserXtrCounters
import com.vk.sdk.api.users.methods.UsersGet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel : BaseViewModel() {

    val userProfile = MutableLiveData<UsersUserXtrCounters>()

    fun getProfileInfo() {
        launchSafety {
            val user = VK.executeSync(
                UsersGet(fields = listOf(UsersFields.PHOTO_MAX_ORIG, UsersFields.PHOTO_MAX, UsersFields.PHOTO_400_ORIG))
            ).first()
            userProfile.postValue(user)
        }
    }

}