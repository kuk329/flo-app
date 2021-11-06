package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding

    private var gson : Gson = Gson()

    private val information = arrayListOf("수록곡","상세정보","영상")



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentAlbumBinding.inflate(inflater, container,false)

        // Home 에서 넘어온 데이터 받아오기
        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData,Album::class.java) // json 형식을 다시 Album 객체 형태로 변환
        // Home fragment 에서 넘어온 데이터를 화면에 반영
        setInit(album)


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

    private fun setInit(album: Album) {
        binding.albumAlbumIv.setImageResource(album.coverImg!!)
        binding.albumMusicTitleTv.text = album.title
        binding.albumSingerTv.text = album.singer
    }

}