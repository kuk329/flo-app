package com.example.flo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FragmentAdapter (fragmentManager: FragmentManager):FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int=3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SongListFragment().newInstance()
            1 -> DetailFragment().newInstance()
            2 -> VideoFragment().newInstance()
            else -> SongListFragment().newInstance()
        }
    }


}