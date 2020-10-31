package com.example.vkcloud.ui.albums

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vkcloud.R
import com.vk.sdk.api.photos.dto.PhotosPhoto
import kotlinx.android.synthetic.main.item_photo.view.*

class AlbumAdapter(
    private val onClickPhoto: (PhotosPhoto) -> Unit
) : ListAdapter<PhotosPhoto, AlbumViewHolder>(AlbumDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return AlbumViewHolder(view, parent)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(getItem(position), onClickPhoto)
    }
}

class AlbumViewHolder(view: View, private val parent: ViewGroup) : RecyclerView.ViewHolder(view) {
    fun bind(item: PhotosPhoto, onClickPhoto: (PhotosPhoto) -> Unit) {
        itemView.setOnClickListener { onClickPhoto(item) }

        Glide.with(itemView)
            .load(item.photo256 ?: item.sizes?.maxBy { it.width }?.url)
            .into(itemView.album_photo)
    }
}

class AlbumDiffUtilCallback : DiffUtil.ItemCallback<PhotosPhoto>() {
    override fun areItemsTheSame(oldItem: PhotosPhoto, newItem: PhotosPhoto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PhotosPhoto, newItem: PhotosPhoto): Boolean {
        return oldItem == newItem
    }
}