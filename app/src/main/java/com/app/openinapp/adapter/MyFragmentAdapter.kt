package com.app.openinapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.openinapp.fragment.RecentLinksFragment
import com.app.openinapp.fragment.TopLinksFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


class MyFragmentAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> RecentLinksFragment()
            else -> TopLinksFragment()
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}
