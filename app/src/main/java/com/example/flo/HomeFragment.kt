package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson
import com.google.gson.JsonArray


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private var albums = ArrayList<Album>()

    private lateinit var songDB : SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // ROOM DB
        songDB = SongDatabase.getInstance(requireContext())!!
        albums.addAll(songDB.albumDao().getAlbums()) // songDB에서 ALBUM list를 가져온다.


        // 레이아웃 메니저 설정
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)


        // 더미 데이터와 Adapter 연결
        val albumRvAdapter = AlbumRvAdapter(albums)


        albumRvAdapter.setMyItemClickListener(object:AlbumRvAdapter.MyItemClickListener{
            override fun onItemClick(album:Album) {
                startAlbumFragment(album)
            }
        })

        // 리사이클러뷰에 어댑터 연결
        binding.homeTodayMusicAlbumRv.adapter = albumRvAdapter



        // viewPagerAdapter1
        val backgroundAdapter = HomeBackgroundViewPagerAdapter(this)

        binding.homeBackgroundVp.adapter = backgroundAdapter

        binding.homeIndicator.setViewPager(binding.homeBackgroundVp)




        // viewPagerAdapter2
        val bannerAdapter = BannerViewPagerAdapter(this)
        // 이미지 여러개 추가
        bannerAdapter.addFragment(BannerFragment(R.drawable.banner1))
        bannerAdapter.addFragment(BannerFragment(R.drawable.banner2))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation =ViewPager2.ORIENTATION_HORIZONTAL // 좌우 상하 스크롤을 지정 (default 는 좌우)


        return binding.root
    }

    private fun startAlbumFragment(album: Album) { // fragment 간에 데이터 전달을 위해 bundle 사용
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply { // fragment 간 데이터 교환
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }// end of startAlbumFragment


}// end of class