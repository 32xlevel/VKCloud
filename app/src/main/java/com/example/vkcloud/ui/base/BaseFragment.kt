package com.example.vkcloud.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.vkcloud.R
import com.example.vkcloud.viewmodel.BaseViewModel
import com.example.vkcloud.viewmodel.ProgressOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

abstract class BaseFragment<VM : BaseViewModel>(@LayoutRes layoutRes: Int) : Fragment(layoutRes) {
    abstract val viewModel: VM

    private var progressDialog: AlertDialog? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.progress.observe(viewLifecycleOwner, ::observeProgress)
        viewModel.message.observe(viewLifecycleOwner, ::showSnack)
    }

    open fun observeProgress(progress: ProgressOptions) {
        requireActivity().progress.isVisible = progress.withProgress

        if (progress.withProgress && progress.blocked) {
            progressDialog = AlertDialog.Builder(requireContext())
                .setView(R.layout.progress_layout)
                .setCancelable(false)
                .create()
            progressDialog?.show()
        }
        if (!progress.withProgress) {
            progressDialog?.dismiss()
        }
    }

    private fun showSnack(message: String) {
        val rootView = requireActivity().findViewById<View>(android.R.id.content)
        Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show()
    }
}