package com.example.vkcloud.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.vkcloud.R
import com.example.vkcloud.extensions.openInBrowser
import com.example.vkcloud.viewmodel.ProfileViewModel
import com.vk.api.sdk.VK
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().toolbar.isGone = true

        viewModel.getProfileInfo()
        viewModel.userProfile.observe(viewLifecycleOwner) {
            if (it == null) return@observe

            profile_firstname.text = getString(R.string.profile_name, it.firstName, it.lastName)
            Glide.with(this).load(it.photoMax).circleCrop().into(profile_photo)
        }

        profile_logout.setOnClickListener {
            VK.logout()
            findNavController().navigate(R.id.action_auth)
        }
        profile_goto_vk.setOnClickListener { openVk() }

        profile_connect_vk_combo.setOnClickListener { Toast.makeText(context, "Soon", Toast.LENGTH_LONG).show() }
        profile_connect_sber.setOnClickListener { Toast.makeText(context, "Soon", Toast.LENGTH_LONG).show() }
        profile_connect_s.setOnClickListener { Toast.makeText(context, "Soon", Toast.LENGTH_LONG).show() }
    }

    override fun onDestroyView() {
        requireActivity().toolbar.isVisible = true
        super.onDestroyView()
    }

    private fun openVk() = "http://vk.com".toUri().openInBrowser(requireContext())
}