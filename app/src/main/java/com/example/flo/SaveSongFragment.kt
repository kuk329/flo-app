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

        albumDatas.apply {
            add(Album("Butter","방탄소년단",R.drawable.song4_img))
            add(Album("Permission to Dance","방탄소년단",R.drawable.song5_img))
            add(Album("Yours","진",R.drawable.song6_img))
            add(Album("나 그댈위해 시 한편을 쓰겠어","케이시(Kassy)",R.drawable.song7_img))
            add(Album("너는 내 세상이었어","볼빨간사춘기",R.drawable.song8_img))
            add(Album("strawberry moon","아이유(IU)",R.drawable.song2_img))
            add(Album("나비효과","볼빨간사춘기",R.drawable.song7_img))
            add(Album("나의 첫사랑","다비치",R.drawable.song9_img))
            add(Album("고백","멜로망스",R.drawable.song10_img))
            add(Album("고백","멜로망스",R.drawable.song10_img))
            add(Album("고백","멜로망스",R.drawable.song10_img))
            add(Album("고백","멜로망스",R.drawable.song10_img))
        }

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