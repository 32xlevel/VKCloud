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
package com.vk.sdk.api.wall.methods

import com.example.vkapi.ApiRequestBase
import com.vk.sdk.api.GsonHolder
import com.vk.sdk.api.base.dto.BaseOkResponseDto
import com.vk.sdk.api.base.responses.BaseOkResponse
import com.vk.sdk.api.wall.dto.WallReportCommentReason
import kotlin.Int
import org.json.JSONObject

/**
 * Reports (submits a complaint about) a comment on a post on a user wall or community wall.
 * @param ownerId ID of the user or community that owns the wall. 
 * @param commentId Comment ID. minimum 0
 * @param reason Reason for the complaint: '0' - spam, '1' - child pornography, '2' - extremism,
 * '3' - violence, '4' - drug propaganda, '5' - adult material, '6' - insult, abuse minimum 0
 */
class WallReportComment(
    private val ownerId: Int,
    private val commentId: Int,
    private val reason: WallReportCommentReason? = null
) : ApiRequestBase<BaseOkResponseDto>(methodName = "wall.reportComment") {
    init {
        addParam("owner_id", ownerId)
        addParam("comment_id", commentId)
        reason?.let { value ->
            addParam("reason", value.value)
        }
    }

    override fun parse(r: JSONObject): BaseOkResponseDto = GsonHolder.gson.fromJson(r.toString(),
            BaseOkResponse::class.java).response
}
