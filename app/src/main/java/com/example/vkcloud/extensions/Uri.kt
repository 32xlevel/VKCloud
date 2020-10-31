package com.example.vkcloud.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import java.io.File

fun Uri.openInBrowser(context: Context): Boolean =
    try {
        val browserIntent = Intent(Intent.ACTION_VIEW, this)
        context.startActivity(browserIntent)
        true
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
        false
    }

fun Uri.toFile(context: Context): File {
    if (this.scheme == "file") {
        if (this.path != null) return File(this.path!!)
        throw IllegalArgumentException()
    }
    val proj = arrayOf(MediaStore.Files.FileColumns.DATA)
    val cursor = context.contentResolver.query(this, proj, null, null, null)
    val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    cursor.moveToFirst()
    return File("file://" + cursor.getString(columnIndex))
}