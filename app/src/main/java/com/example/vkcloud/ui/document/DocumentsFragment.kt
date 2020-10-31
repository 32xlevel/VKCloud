package com.example.vkcloud.ui.document

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.vkcloud.R
import com.example.vkcloud.extensions.openInBrowser
import com.example.vkcloud.ui.base.BaseFragment
import com.example.vkcloud.utils.ContentUriUtils
import com.example.vkcloud.viewmodel.DocumentsViewModel
import kotlinx.android.synthetic.main.fragment_documents.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class DocumentsFragment : BaseFragment<DocumentsViewModel>(R.layout.fragment_documents) {

    private companion object {
        private const val RC_ADD_DOCUMENT = 1
    }

    override val viewModel: DocumentsViewModel by viewModels()
    private lateinit var adapter: DocumentsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DocumentsAdapter(
            onRenameDocument = viewModel::renameDocument,
            onDeleteDocument = ::deleteDocument,
            onClickDocument = { tryOpenDocument(it.url) }
        )
        documents_recycler.adapter = adapter

        viewModel.loadDocuments()
        viewModel.docs.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.isSearch.observe(viewLifecycleOwner) {
            if (!it) {
                adapter.submitList(viewModel.docs())
            }
        }
        viewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            adapter.submitList(viewModel.docs().filter { it.title.contains(query, ignoreCase = true) })
        }

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.documents_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add) {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType("*/*")
            startActivityForResult(intent, RC_ADD_DOCUMENT)

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_ADD_DOCUMENT && resultCode == Activity.RESULT_OK && data != null) {
            data.data?.let { uri ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val file = prepareFile(uri)
                    viewModel.addDocument(file)
                }
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val menuItem = menu.findItem(R.id.action_search)
        val searchView = menuItem.actionView as SearchView

        if (viewModel.isSearch()) {
            menuItem.expandActionView()
            searchView.setQuery(viewModel.searchQuery(), false)
        }

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                viewModel.handleSearchMode(true)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                viewModel.handleSearchMode(false)
                return true
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.handleSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.handleSearch(newText)
                return true
            }
        })

        searchView.setOnCloseListener {
            viewModel.handleSearchMode(false)
            true
        }
    }

    // *cough* rewrite
    private fun prepareFile(uri: Uri): File {
        val filePath = ContentUriUtils.getFilePath(requireContext(), uri)

        val fileExtension = filePath?.substringAfterLast(".") ?: "file"
        val fileName = filePath?.substring(
            filePath.lastIndexOf('/') + 1,
            filePath.lastIndexOf('.')
        ) ?: "cloud"

        val bytes = requireContext().contentResolver.openInputStream(uri).use { it!!.readBytes() }

        val file = File(requireContext().cacheDir, "${fileName}.${fileExtension}")
        file.outputStream().use {
            it.write(bytes)
            it.flush()
        }

        return file
    }

    private fun deleteDocument(docId: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle("Вы действительно хотите удалить документ?")
            .setPositiveButton("Да") { _, _ -> viewModel.deleteDocument(docId) }
            .show()
    }

    private fun tryOpenDocument(url: String?) = url?.toUri()?.openInBrowser(requireContext())

}