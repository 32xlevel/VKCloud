/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 vk.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
*/
// *********************************************************************
// THIS FILE IS AUTO GENERATED!
// DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING.
// *********************************************************************
package com.vk.sdk.api.messages.methods

import com.example.vkapi.ApiRequestBase
import com.vk.sdk.api.GsonHolder
import com.vk.sdk.api.messages.dto.MessagesSetChatPhotoResponseDto
import com.vk.sdk.api.messages.responses.MessagesSetChatPhotoResponse
import kotlin.String
import org.json.JSONObject

/**
 * Sets a previously-uploaded picture as the cover picture of a chat.
 * @param file Upload URL from the 'response' field returned by the
 * [vk.com/dev/photos.getChatUploadServer|photos.getChatUploadServer] method upon successfully
 * uploading an image. 
 */
class MessagesSetChatPhoto(
    private val file: String
) : ApiRequestBase<MessagesSetChatPhotoResponseDto>(methodName = "messages.setChatPhoto") {
    init {
        addParam("file", file)
    }

    override fun parse(r: JSONObject): MessagesSetChatPhotoResponseDto =
            GsonHolder.gson.fromJson(r.toString(),
            MessagesSetChatPhotoResponse::class.java).response
}
