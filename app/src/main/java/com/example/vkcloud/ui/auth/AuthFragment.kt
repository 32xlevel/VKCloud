package com.example.vkcloud.ui.auth

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.vkcloud.R
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment : Fragment(R.layout.fragment_auth) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().appbar.isGone = true
        requireActivity().nav_view.isGone = true

        auth_button.setOnClickListener {
            VK.login(requireActivity(), arrayListOf(VKScope.DOCS, VKScope.PHOTOS))
        }
    }

    override fun onDestroyView() {
        requireActivity().appbar.isVisible = true
        requireActivity().nav_view.isVisible = true
        super.onDestroyView()
    }

}