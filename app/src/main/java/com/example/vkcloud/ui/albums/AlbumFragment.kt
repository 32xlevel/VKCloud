package com.example.vkcloud.ui.albums

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vkcloud.R
import com.example.vkcloud.extensions.dpToPx
import com.example.vkcloud.ui.utils.GridSpacingItemDecoration
import com.example.vkcloud.viewmodel.AlbumViewModel
import kotlinx.android.synthetic.main.fragment_album.*

class AlbumFragment : Fragment(R.layout.fragment_album) {
    private companion object {
        private const val RC_ADD_PHOTO = 1
    }

    private val viewModel: AlbumViewModel by viewModels()
    private val args: AlbumFragmentArgs by navArgs()

    private lateinit var adapter: AlbumAdapter
    private lateinit var itemDecoration: GridSpacingItemDecoration

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(args.album.canUpload?.value == 1 || args.album.id > 0)

        adapter = AlbumAdapter { findNavController().navigate(AlbumFragmentDirections.actionFragmentAlbumToFragmentPhoto(viewModel.photos.value!!.toTypedArray(), it)) }
        itemDecoration = GridSpacingItemDecoration(3, 1.dpToPx(), true)
        album_recycler.layoutManager = GridLayoutManager(context, 3)
        album_recycler.addItemDecoration(itemDecoration)
        album_recycler.adapter = adapter
        album_recycler.setHasFixedSize(true)

        viewModel.getPhotos(args.album.id)
        viewModel.photos.observe(viewLifecycleOwner) { adapter.submitList(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.album_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("image/*")
            startActivityForResult(intent, RC_ADD_PHOTO)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_ADD_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
            data.data?.let {
                viewModel.uploadPhoto(requireContext().contentResolver.openInputStream(it), args.album.id)
            }
        }
    }

}