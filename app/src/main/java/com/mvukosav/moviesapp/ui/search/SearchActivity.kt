package com.mvukosav.moviesapp.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvukosav.moviesapp.R
import com.mvukosav.moviesapp.databinding.ActivitySearchBinding
import com.mvukosav.moviesapp.presentation.home.adapters.MoviesRecyclerViewAdapter
import com.mvukosav.moviesapp.presentation.home.adapters.MoviesRowRecyclerViewAdapter
import com.mvukosav.moviesapp.presentation.search.SearchActivityViewModel
import com.mvukosav.moviesapp.presentation.search.adapters.SearchRecyclerViewAdapter
import com.mvukosav.moviesapp.ui.details.MovieDetailsActivity
import com.mvukosav.moviesapp.utils.setGone
import com.mvukosav.moviesapp.utils.setVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), MoviesRowRecyclerViewAdapter.OnMovieClickListener {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchRecyclerViewAdapter: SearchRecyclerViewAdapter
    private val viewModel: SearchActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.txtToolbarTitle.text = this.getString(R.string.search)
        setOnClickListener()
        initRecyclerViews()
        observeSearch()
    }

    private fun setOnClickListener() {
        binding.searchInputText.doOnTextChanged { text, start, before, count ->
            viewModel.fetchSearchResult(text.toString())
            if (count == 0) {
                viewModel.viewModelScope.launch {
                    binding.warningLabel.setVisible()
                    delay(2000)
                    binding.warningLabel.setGone()
                }
            }
        }

        binding.toolbar.btnToolbarStart.setOnClickListener {
            finish()
        }
    }

    private fun initRecyclerViews() {
        binding.searchResultRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            searchRecyclerViewAdapter = SearchRecyclerViewAdapter(context, this@SearchActivity)
            adapter = searchRecyclerViewAdapter
        }
    }

    private fun observeSearch() {
        viewModel.fetchSearchMoviesLiveData.observe(this) {
            if (it != null) {
                searchRecyclerViewAdapter.swapList(it)
                binding.searchResultRecyclerView.setVisible()
                binding.noMoviesLayout.setGone()
            } else {
                binding.noMoviesLayout.setVisible()
                binding.searchResultRecyclerView.setGone()
            }
        }
    }


    private fun openDetailsScreen(movieId: String) {
        val i = MovieDetailsActivity.createIntent(this)
        i.putExtra("MovieId", movieId)
        startActivity(i)
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, SearchActivity::class.java)
    }

    override fun onMovieClicked(movieId: String) {
        openDetailsScreen(movieId)
    }
}