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
package com.vk.sdk.api.board.methods

import com.example.vkapi.ApiRequestBase
import com.vk.sdk.api.GsonHolder
import com.vk.sdk.api.base.dto.BaseOkResponseDto
import com.vk.sdk.api.base.responses.BaseOkResponse
import kotlin.Int
import kotlin.String
import org.json.JSONObject

/**
 * Edits the title of a topic on a community's discussion board.
 * @param groupId ID of the community that owns the discussion board. minimum 0
 * @param topicId Topic ID. minimum 0
 * @param title New title of the topic. 
 */
class BoardEditTopic(
    private val groupId: Int,
    private val topicId: Int,
    private val title: String
) : ApiRequestBase<BaseOkResponseDto>(methodName = "board.editTopic") {
    init {
        addParam("group_id", groupId)
        addParam("topic_id", topicId)
        addParam("title", title)
    }

    override fun parse(r: JSONObject): BaseOkResponseDto = GsonHolder.gson.fromJson(r.toString(),
            BaseOkResponse::class.java).response
}
