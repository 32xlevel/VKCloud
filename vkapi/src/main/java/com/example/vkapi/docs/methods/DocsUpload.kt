package com.example.vkapi.docs.methods

import com.vk.sdk.api.GsonHolder
import com.vk.sdk.api.docs.dto.DocsDocUploadResponse
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class DocsUpload {
    fun upload(server: String, file: File): DocsDocUploadResponse {
        val part = MultipartBody.Part.createFormData("file", file.name, RequestBody.create("*/*".toMediaType(), file))
        val body = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addPart(part)
            .build()

        val request = Request.Builder()
            .url(server)
            .post(body)
            .build()

        val response = OkHttpClient().newCall(request).execute()
        val json = response.body!!.string()

        return GsonHolder.gson.fromJson(json, DocsDocUploadResponse::class.java)
    }
}