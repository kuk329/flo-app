package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator


class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding

    private val information = arrayListOf("수록곡","상세정보","영상")



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentAlbumBinding.inflate(inflater, container,false)

        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm,HomeFragment())
                .commitAllowingStateLoss()
        }

        binding.albumAlbumIv.clipToOutline = true // 이미지 모서리 둥굴게

        val albumAdapter = AlbumViewPagerAdapter(this)
        binding.albumContentVp.adapter = albumAdapter

        // TabLayout 과 ViewPager 연결
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){
            tab,position ->
            tab.text = information[position]
        }.attach()


        return binding.root


    }

}