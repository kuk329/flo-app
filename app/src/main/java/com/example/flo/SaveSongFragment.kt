package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSaveSongBinding

class SaveSongFragment: Fragment() {

    lateinit var  binding : FragmentSaveSongBinding
    private var albumDatas = ArrayList<Album>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        binding = FragmentSaveSongBinding.inflate(inflater,container,false)


        val saveSongAdapter = SaveSongRvAdapter(albumDatas)
        binding.saveSongRecyclerview.adapter = saveSongAdapter


        // 인터페이스 구현
        saveSongAdapter.setMyItemClickListener(object : SaveSongRvAdapter.MyItemClickListener{
            override fun onRemoveAlbum(position: Int) {
                saveSongAdapter.removeItem(position)
            }

        })


        // layoutManager 설정
        binding.saveSongRecyclerview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)






        return binding.root
    }
}