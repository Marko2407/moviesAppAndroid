package com.mvukosav.moviesapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mvukosav.moviesapp.databinding.FragmentHomeBinding
import com.mvukosav.moviesapp.databinding.FragmentWatchListBinding
import com.mvukosav.moviesapp.presentation.home.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WatchListFragment : Fragment() {

    private lateinit var binding: FragmentWatchListBinding
    private val homeViewModel: HomeFragmentViewModel by viewModels()


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
    }

    private fun initRecyclerView() {
        //TODO("Not yet implemented")
    }

    companion object {
    }
}