package com.example.vkcloud.ui.albums.photo

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.vkcloud.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_photo.*

class PhotoFragment : Fragment(R.layout.fragment_photo) {

    private val args: PhotoFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().toolbar.isGone = true
        requireActivity().nav_view.isGone = true

        back_button.setOnClickListener { findNavController().navigateUp() }

        photo_viewpager.adapter = PhotoAdapter(args.photos)
        photo_viewpager.setCurrentItem(args.photos.indexOf(args.choosenPhoto), false)
    }

    override fun onDestroyView() {
        requireActivity().toolbar.isVisible = true
        requireActivity().nav_view.isVisible = true

        super.onDestroyView()
    }
}