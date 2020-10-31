package com.example.vkcloud.ui.albums.photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vkcloud.R
import com.vk.sdk.api.photos.dto.PhotosPhoto
import kotlinx.android.synthetic.main.item_photo_page.view.*

class PhotoAdapter(
    private val photos: Array<PhotosPhoto>
) : RecyclerView.Adapter<PhotoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo_page, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount() = photos.size
}

class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: PhotosPhoto) {
        Glide.with(itemView)
            .load(item.sizes?.maxByOrNull { it.width }?.url ?: item.photo256)
            .into(itemView.photo)
    }
}