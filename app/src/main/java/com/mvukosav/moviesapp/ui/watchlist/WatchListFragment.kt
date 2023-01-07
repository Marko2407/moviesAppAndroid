package com.mvukosav.moviesapp.ui.watchlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvukosav.moviesapp.databinding.FragmentWatchListBinding
import com.mvukosav.moviesapp.presentation.watchlist.WatchListFragmentViewModel
import com.mvukosav.moviesapp.presentation.watchlist.adapters.OnWatchListClickListener
import com.mvukosav.moviesapp.presentation.watchlist.adapters.WatchListRecyclerViewAdapter
import com.mvukosav.moviesapp.ui.details.MovieDetailsActivity
import com.mvukosav.moviesapp.utils.setGone
import com.mvukosav.moviesapp.utils.setVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchListFragment : Fragment(), OnWatchListClickListener {

    private lateinit var binding: FragmentWatchListBinding
    private val viewModel: WatchListFragmentViewModel by viewModels()
    private lateinit var watchListRecyclerView: WatchListRecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWatchListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeWatchList()
        observeUpdatedWatchList()
    }

    private fun initRecyclerView() {
        binding.watchListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            watchListRecyclerView =
                WatchListRecyclerViewAdapter(requireContext(), this@WatchListFragment)
            adapter = watchListRecyclerView
        }
    }

    private fun observeWatchList() {
        viewModel.fetchWatchListMoviesLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                watchListRecyclerView.swapList(it)
                if (it.isEmpty()) {
                    binding.watchListRecyclerView.setGone()
                    binding.noMoviesLayout.setVisible()
                } else {
                    binding.watchListRecyclerView.setVisible()
                    binding.noMoviesLayout.setGone()
                }
            } else {
                //set btn visible
                //set view visible
            }
        }
    }

    private fun observeUpdatedWatchList() {
        viewModel.fetchUpdatedMovieLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                Toast.makeText(requireContext(), "Successfully removed item", Toast.LENGTH_SHORT)
                    .show()
                watchListRecyclerView.swapList(it)
                if (it.isEmpty()) {
                    binding.watchListRecyclerView.setGone()
                    binding.noMoviesLayout.setVisible()
                } else {
                    binding.watchListRecyclerView.setVisible()
                    binding.noMoviesLayout.setGone()
                }
            } else {
                Toast.makeText(requireContext(), "Unknown error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.initDataFetch()
    }

    companion object {
    }

    override fun onDetailsClicked(movieId: String) {
        val i = MovieDetailsActivity.createIntent(requireContext())
        i.putExtra("MovieId", movieId)
        startActivity(i)
    }

    override fun onRemoveClicked(movieId: String) {
        viewModel.removeFromWatchList(movieId)
    }
}