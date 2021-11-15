package com.example.flo

import android.os.Bundle
import android.util.Log
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
    private var albumDatas = ArrayList<Album>() // 여러 앨범정보들을 담고있는 arrayList 선언
    private var songDatas = ArrayList<Song>()
    private var gson : Gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

//        binding.homeTodayReleaseMusic1Cv.setOnClickListener {
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.main_frm , AlbumFragment())
//                .commitAllowingStateLoss()
//        }

        songDatas.apply {
            add(Song("01","IF I","백지영",0,221,false,"music_if_i",true))
            add(Song("02","dsf","백지영",0,221,false,"music_if_i",false))
            add(Song("03","dfd","백지영",0,221,false,"music_if_i",true))
        }
        // 데이터 리스트 생성 (더미 데이터) -> 리사이클러 뷰로 나타낼 데이터들을 수동으로 넣어서 어댑터 클래스에 넘김
        albumDatas.apply {
            add(Album("연모 OST Part.3","백지영",R.drawable.song1_img,songDatas))
            add(Album("strawberry moon","아이유(IU)",R.drawable.song2_img,songDatas))
            add(Album("CRAZY IN LOVE","ITZY (있지)",R.drawable.today_release_music1,songDatas))
            add(Album("9월 24일","임한별",R.drawable.today_release_music2,songDatas))
            add(Album("My Universe","Coldplay &amp; 방탄소년단",R.drawable.today_release_music3,songDatas))
            add(Album("바라만 본다","MSG워너비(M.O.M)",R.drawable.song3_img,songDatas))
        }


        // 어댑터 설정정
        // 더미 데이터와 Adapter 연결
        val albumRvAdapter = AlbumRvAdapter(albumDatas)
        // 리사이클러뷰에 어댑터 연결
        binding.homeTodayMusicAlbumRv.adapter = albumRvAdapter


        albumRvAdapter.setMyItemClickListener(object:AlbumRvAdapter.MyItemClickListener{
            override fun onItemClick(album:Album) {
                changeAlbumFragment(album)
            }
        })

        // 레이아웃 메니저 설정
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)



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

    private fun changeAlbumFragment(album: Album) { // fragment 간에 데이터 전달을 위해 bundle 사용
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()

        val sharedPreferences = this.activity?.getSharedPreferences("songList", AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        val jsonArray = JsonArray()

//        for(i:Int in 0..album.songs!!.size){
//            jsonArray.put()
//        }
//        editor?.putString("songList",json)
//        editor?.apply()
//        Log.d("테스트",""+json)


    }


}// end of class