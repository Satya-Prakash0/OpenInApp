package com.app.openinapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.openinapp.R
import com.app.openinapp.databinding.FragmentTopLinksBinding
import com.app.openinapp.viewmodel.MainViewModel
import com.app.openinapp.adapter.TopLinkAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TopLinksFragment @Inject constructor()  : Fragment(R.layout.fragment_top_links) {

    lateinit var binding: FragmentTopLinksBinding
    lateinit var mainViewModel: MainViewModel
    @Inject lateinit var adapter: TopLinkAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding= FragmentTopLinksBinding.bind(view)
        val layoutManager = LinearLayoutManager(requireContext())
        binding.toplinkRecyclerView.layoutManager=layoutManager

        binding.toplinkRecyclerView.adapter=adapter

        mainViewModel= ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        mainViewModel.dashBoardData.observe(viewLifecycleOwner){
            adapter.setTopLinks(it.data.top_links)
        }


    }

}