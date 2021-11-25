package com.example.flo

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class LockerViewPageAdapter(fragment:Fragment):FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int=3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->SaveSongFragment()
            1->SaveMusicFileFragment()
            else-> SaveAlbumFragment()

        }
    }
}