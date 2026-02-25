package com.example.perfectnotes.presentation.ui.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.perfectnotes.R
import com.example.perfectnotes.databinding.ActivityMainBinding
import com.example.perfectnotes.presentation.adapter.NoteAdapter
import com.example.perfectnotes.presentation.viewmodel.MainViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.search.SearchView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var searchView: SearchView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupToolbar()
        setupSearchView()
        setupRecyclerView()
        setupObservers()
        setupNavigationDrawer()
        setupClickListeners()
    }
    
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
    }
    
    private fun setupSearchView() {
        searchView = SearchView(this)
        searchView.setupWithSearchBar(binding.searchBar)
        
        searchView.editText.setOnEditorActionListener { _, _, _ ->
            val query = searchView.text.toString()
            viewModel.setSearchQuery(query)
            searchView.hide()
            false
        }
        
        searchView.addTransitionListener { _, _, newState ->
            if (newState == SearchView.TransitionState.HIDDEN) {
                viewModel.setSearchQuery("")
            }
        }
    }
    
    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(
            viewMode = viewModel.viewMode.value,
            onItemClick = { note ->
                // Navigasi ke detail
                showToast("Detail: ${note.title}")
            },
            onPinClick = { note ->
                viewModel.togglePinNote(note)
            },
            onArchiveClick = { note ->
                viewModel.toggleArchiveNote(note)
            },
            onDeleteClick = { note ->
                showDeleteConfirmation(note)
            }
        )
        
        binding.recyclerView.apply {
            adapter = noteAdapter
            setHasFixedSize(true)
        }
        
        viewModel.viewMode.observe(this) { mode ->
            updateLayoutManager(mode)
            noteAdapter = NoteAdapter(
                viewMode = mode,
                onItemClick = { note -> showToast("Detail: ${note.title}") },
                onPinClick = { note -> viewModel.togglePinNote(note) },
                onArchiveClick = { note -> viewModel.toggleArchiveNote(note) },
                onDeleteClick = { note -> showDeleteConfirmation(note) }
            )
            binding.recyclerView.adapter = noteAdapter
        }
    }
    
    private fun updateLayoutManager(mode: MainViewModel.ViewMode) {
        binding.recyclerView.layoutManager = when (mode) {
            MainViewModel.ViewMode.GRID -> {
                GridLayoutManager(this, 2)
            }
            MainViewModel.ViewMode.LIST -> {
                LinearLayoutManager(this)
            }
        }
    }
    
    private fun setupObservers() {
        viewModel.notes.observe(this) { notes ->
            noteAdapter.submitList(notes)
            updateEmptyState(notes.isEmpty())
        }
        
        viewModel.categories.observe(this) { categories ->
            setupCategoryChips(categories)
        }
        
        lifecycleScope.launch {
            viewModel.searchQuery.collectLatest { query ->
                binding.searchBar.setText(query)
            }
        }
    }
    
    private fun setupCategoryChips(categories: List<String>) {
        binding.chipGroupCategories.removeAllViews()
        
        categories.forEachIndexed { index, category ->
            val chip = Chip(this).apply {
                text = category
                isCheckable = true
                isChecked = index == 0
                
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        viewModel.setSelectedCategory(category)
                    }
                }
            }
            binding.chipGroupCategories.addView(chip)
        }
    }
    
    private fun updateEmptyState(isEmpty: Boolean) {
        binding.emptyStateView.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
    
    private fun setupNavigationDrawer() {
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_all_notes -> {
                    viewModel.setSelectedCategory("Semua")
                    viewModel.toggleShowArchived()
                    binding.drawerLayout.closeDrawers()
                }
                R.id.nav_archived -> {
                    viewModel.toggleShowArchived()
                    binding.drawerLayout.closeDrawers()
                }
                R.id.nav_trash -> {
                    showToast("Fitur tempat sampah")
                    binding.drawerLayout.closeDrawers()
                }
                R.id.nav_settings -> {
                    showToast("Fitur pengaturan")
                    binding.drawerLayout.closeDrawers()
                }
                R.id.nav_backup -> {
                    showToast("Fitur backup")
                    binding.drawerLayout.closeDrawers()
                }
            }
            true
        }
    }
    
    private fun setupClickListeners() {
        binding.fabAdd.setOnClickListener {
            showToast("Tambah catatan baru")
        }
        
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_view_toggle -> {
                    viewModel.toggleViewMode()
                    true
                }
                R.id.action_search -> {
                    binding.searchBar.show()
                    true
                }
                else -> false
            }
        }
    }
    
    private fun showDeleteConfirmation(note: com.example.perfectnotes.domain.model.Note) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Hapus Catatan")
            .setMessage("Apakah Anda yakin ingin menghapus catatan '${note.title}'?")
            .setPositiveButton("Hapus") { _, _ ->
                viewModel.deleteNote(note)
                showToast("Catatan dipindahkan ke tempat sampah")
            }
            .setNegativeButton("Batal", null)
            .show()
    }
    
    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            binding.drawerLayout.open()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}