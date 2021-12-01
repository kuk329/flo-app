package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding

    private var gson : Gson = Gson()

    private val information = arrayListOf("수록곡","상세정보","영상")

    private var isLiked : Boolean = false



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

        isLiked = isLikedAlbum(album.id)
        setInit(album)
        setClickListeners(album)


        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm,HomeFragment())
                .commitAllowingStateLoss()
        }

        binding.albumAlbumIv.clipToOutline = true // 엘범 이미지 모서리 둥굴게

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
        Log.d("albumImg :",album.coverImg.toString())
        binding.albumMusicTitleTv.text = album.title
        binding.albumSingerTv.text = album.singer

        if(isLiked){
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.albumAlbumIv.setImageResource(R.drawable.ic_my_like_off_)
        }
    }

    private fun setClickListeners(album:Album){
        val userId:Int = getJwt()

        binding.albumLikeIv.setOnClickListener {
            if(isLiked){ // 좋아요가 이미 되어있을때 한번 더 누르면 -> 좋아요 취소
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off_)
                disLikeAlbum(userId,album.id)

            }else{ // 좋아요 처리
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId,album.id)

            }
        }
    }

    private fun likeAlbum(userId:Int, albumId:Int){ // 엘범 좋아요 할때 실행
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId,albumId)
        songDB.albumDao().likeAlbum(like)
    }

    private fun isLikedAlbum(albumId:Int):Boolean{
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt() // 현재 로그인한 유저 아이디(jwt) 가져옴

        val likeId :Int? = songDB.albumDao().isLikeAlbum(userId,albumId)

        if(likeId==null) return false // 좋아요가 되어있지 않으면 return false

        return true
    }

    private fun disLikeAlbum(userId:Int,albumId:Int){
        val songDB = SongDatabase.getInstance(requireContext())!!


        songDB.albumDao().disLikeAlbum(userId,albumId)

    }

    private fun getJwt():Int{
        val spf = activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)

        return spf!!.getInt("jwt",0)
    }


    // Room DB
    private fun getSongs(albumIdx:Int):ArrayList<Song>{
        val songDB = SongDatabase.getInstance(requireContext())!!

        val songs = songDB.songDao().getSongsInAlbum(albumIdx) as ArrayList

        return songs
    }

}