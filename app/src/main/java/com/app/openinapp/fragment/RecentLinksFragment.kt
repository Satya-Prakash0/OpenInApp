package com.app.openinapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.openinapp.R
import com.app.openinapp.databinding.FragmentRecentLinksBinding
import com.app.openinapp.viewmodel.MainViewModel
import com.app.openinapp.adapter.RecentLinkAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecentLinksFragment @Inject constructor() : Fragment(R.layout.fragment_recent_links) {

    lateinit var binding: FragmentRecentLinksBinding
    lateinit var mainViewModel: MainViewModel
    @Inject lateinit var adapter: RecentLinkAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding= FragmentRecentLinksBinding.bind(view)
        val layoutManager=LinearLayoutManager(requireContext())

        binding.recentRecycle.layoutManager=layoutManager
        binding.recentRecycle.adapter=adapter
        mainViewModel= ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        mainViewModel.dashBoardData.observe(viewLifecycleOwner){
            adapter.setTopLinks(it.data.recent_links)
        }

    }
}