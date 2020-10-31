package com.example.vkcloud.ui.albums

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vkcloud.R
import com.example.vkcloud.viewmodel.AlbumsMode
import com.vk.sdk.api.photos.dto.PhotosPhotoAlbumFull
import kotlinx.android.synthetic.main.item_album.view.*

private var albumsMode: AlbumsMode = AlbumsMode.WATCH

class AlbumsAdapter(
    private val onRemoveClick: (PhotosPhotoAlbumFull) -> Unit,
    private val onAlbumClick: (PhotosPhotoAlbumFull) -> Unit
) : ListAdapter<PhotosPhotoAlbumFull, AlbumsViewHolder>(AlbumsDiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return AlbumsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumsViewHolder, position: Int) {
        holder.bind(getItem(position), onRemoveClick, onAlbumClick)
    }

    fun changeAlbumsMode(mode: AlbumsMode) {
        albumsMode = mode
        notifyDataSetChanged()
    }
}

class AlbumsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(
        item: PhotosPhotoAlbumFull,
        onRemoveClick: (PhotosPhotoAlbumFull) -> Unit,
        onAlbumClick: (PhotosPhotoAlbumFull) -> Unit
    ) {
        itemView.album_thumbnail.clipToOutline = true
        Glide.with(itemView)
            .load(item.sizes?.maxByOrNull { it.width }?.src ?: item.thumbSrc)
            .into(itemView.album_thumbnail)
        itemView.album_title.text = item.title
        itemView.album_photos_count.text =
            itemView.resources.getQuantityString(R.plurals.album_count_photos, item.size, item.size)

        itemView.album_remove_button.setOnClickListener { onRemoveClick(item) }

        when (albumsMode) {
            AlbumsMode.WATCH -> {
                itemView.setOnClickListener { onAlbumClick(item) }
                itemView.album_remove_button.isGone = true
                itemView.album_gray_overlay.isGone = true
            }
            AlbumsMode.EDIT -> {
                itemView.setOnClickListener(null)
                itemView.album_remove_button.isVisible = item.id > 0
                itemView.album_gray_overlay.isGone = item.id > 0
            }
        }
    }
}

class AlbumsDiffUtilCallback : DiffUtil.ItemCallback<PhotosPhotoAlbumFull>() {
    override fun areItemsTheSame(
        oldItem: PhotosPhotoAlbumFull,
        newItem: PhotosPhotoAlbumFull
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PhotosPhotoAlbumFull,
        newItem: PhotosPhotoAlbumFull
    ): Boolean {
        return oldItem == newItem
    }
}