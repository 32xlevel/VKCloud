package com.example.vkcloud.ui.document

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isGone
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vkcloud.R
import com.example.vkcloud.extensions.humanReadableBytes
import com.vk.sdk.api.docs.dto.DocsDoc
import kotlinx.android.synthetic.main.item_document.view.*

class DocumentsAdapter(
    private val onRenameDocument: (id: Int, title: String) -> Unit,
    private val onDeleteDocument: (id: Int) -> Unit,
    private val onClickDocument: (document: DocsDoc) -> Unit
) : ListAdapter<DocsDoc, DocumentsViewHolder>(DocumentsDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_document, parent, false)
        return DocumentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int) {
        holder.bind(getItem(position), onRenameDocument, onDeleteDocument, onClickDocument)
    }
}

class DocumentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    // TODO: split into small functions
    fun bind(
        item: DocsDoc,
        onRenameDocument: (id: Int, title: String) -> Unit,
        onDeleteDocument: (id: Int) -> Unit,
        onClickDocument: (document: DocsDoc) -> Unit
    ) {
        itemView.setOnClickListener { onClickDocument(item) }

        val preview = item.preview?.photo?.sizes?.last()?.src
        val previewFailure = when (item.type) {
            1 -> R.drawable.ic_placeholder_document_text_72
            2 -> R.drawable.ic_placeholder_document_archive_72
            3 -> R.drawable.ic_placeholder_document_video_72
            4 -> R.drawable.ic_placeholder_document_image_72
            5 -> R.drawable.ic_placeholder_document_music_72
            6 -> R.drawable.ic_placeholder_document_video_72
            7 -> R.drawable.ic_placeholder_document_book_72
            else -> R.drawable.ic_placeholder_document_other_72
        }

        itemView.document_image.clipToOutline = true
        Glide.with(itemView)
            .load(preview)
            .error(previewFailure)
            .centerCrop()
            .into(itemView.document_image)

        itemView.document_title.text = item.title
        itemView.document_tags.isGone = item.tags.isEmpty()
        if (!item.tags.isNullOrEmpty()) {
            itemView.document_tags.text = item.tags.joinToString(", ")
        }

        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd")
        val date = java.util.Date(item.date * 1000L)
        val formattedDate = sdf.format(date)

        itemView.document_description.text = itemView.context.getString(
            R.string.documents_description,
            formattedDate,
            item.ext,
            item.size.toLong().humanReadableBytes()
        )

        itemView.document_actions_button.setOnClickListener { v ->
            val popup = PopupMenu(v.context, v)
            popup.inflate(R.menu.document_popup_menu)

            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_rename -> {
                        val editText = EditText(v.context)
                        editText.setText(item.title)

                        AlertDialog.Builder(v.context)
                            .setView(editText)
                            .setPositiveButton("Продолжить") { _, _ ->
                                onRenameDocument(item.id, editText.text.toString())
                            }
                            .show()

                        true
                    }
                    R.id.action_delete -> {
                        onDeleteDocument(item.id)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }
}

class DocumentsDiffUtil : DiffUtil.ItemCallback<DocsDoc>() {
    override fun areItemsTheSame(oldItem: DocsDoc, newItem: DocsDoc): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DocsDoc, newItem: DocsDoc): Boolean {
        return oldItem == newItem
    }
}

