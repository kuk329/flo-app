package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeTodayReleaseMusic1Cv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm , AlbumFragment())
                .commitAllowingStateLoss()
        }


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


}