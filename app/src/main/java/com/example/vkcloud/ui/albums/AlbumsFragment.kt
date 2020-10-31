package com.example.vkcloud.ui.albums

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.vkcloud.R
import com.example.vkcloud.extensions.dpToPx
import com.example.vkcloud.ui.base.BaseFragment
import com.example.vkcloud.ui.utils.GridSpacingItemDecoration
import com.example.vkcloud.viewmodel.AlbumsMode
import com.example.vkcloud.viewmodel.AlbumsViewModel
import com.vk.sdk.api.photos.dto.PhotosPhotoAlbumFull
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_albums.*

class AlbumsFragment : BaseFragment<AlbumsViewModel>(R.layout.fragment_albums) {

    private lateinit var adapter: AlbumsAdapter
    private lateinit var itemDecoration: GridSpacingItemDecoration
    override val viewModel: AlbumsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.loadAlbums()

        adapter = AlbumsAdapter(
            onRemoveClick = {
                AlertDialog.Builder(requireContext())
                    .setTitle("Вы учерены, что хотите удалить альбом?")
                    .setPositiveButton("Да") { _, _ -> viewModel.removeAlbum(it) }
                    .show()
            },
            onAlbumClick = ::openAlbum
        )
        itemDecoration = GridSpacingItemDecoration(2, 16.dpToPx(), true)
        albums_recycler.layoutManager = GridLayoutManager(context, 2)
        albums_recycler.addItemDecoration(itemDecoration)
        albums_recycler.adapter = adapter
        albums_recycler.setHasFixedSize(true)

        viewModel.albums.observe(viewLifecycleOwner) { adapter.submitList(it) }
        viewModel.albumsMode.observe(viewLifecycleOwner) { adapter.changeAlbumsMode(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.albums_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {

                val editText = EditText(context)
                AlertDialog.Builder(requireContext())
                    .setTitle("Задайте заголовок для альбома")
                    .setView(editText)
                    .setPositiveButton("OK") { _, _ ->
                        viewModel.createAlbum(editText.text.toString())
                    }
                    .show()

                true
            }
            R.id.action_edit -> {
                viewModel.setAlbumMode(AlbumsMode.EDIT)

                (requireActivity() as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_cancel_outlne_28)!!.apply {
                        setTint(
                            ContextCompat.getColor(requireContext(), R.color.vk)
                        )
                    }
                )
                (requireActivity() as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
                (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
                (requireActivity() as AppCompatActivity).toolbar.menu.clear()

                true
            }
            android.R.id.home -> {
                viewModel.setAlbumMode(AlbumsMode.WATCH)

                (requireActivity() as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(false)
                (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
                (requireActivity() as AppCompatActivity).supportActionBar?.invalidateOptionsMenu()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun openAlbum(album: PhotosPhotoAlbumFull) {
        findNavController().navigate(AlbumsFragmentDirections.actionNavAlbumsToAlbumFragment(album))
    }
}