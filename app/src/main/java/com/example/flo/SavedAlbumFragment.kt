package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSavedAlbumBinding

class SavedAlbumFragment: Fragment() {

    lateinit var binding : FragmentSavedAlbumBinding
    lateinit var albumDB : SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSavedAlbumBinding.inflate(inflater,container,false)

        albumDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // 리사이클러뷰 데이터 렌더링 <- onStart()는 생명주기에서 화면이 다시 보일때마다 실행되므로 여기서 데이터 렌더링을 해야됨
        initRecyclerView()
    }

    private fun initRecyclerView(){

        val userId = getJwt()

        binding.savedAlbumRecyclerview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        val albumRvAdapter = AlbumLockerRvAdapter()

        albumRvAdapter.setMyItemClickListener(object :AlbumLockerRvAdapter.MyItemClickListener{
            override fun onRemoveSong(albumId: Int) {
                albumDB.albumDao().disLikeAlbum(userId,albumId )
            }
        })
        binding.savedAlbumRecyclerview.adapter = albumRvAdapter

        albumRvAdapter.addSongs(albumDB.albumDao().getLikedAlbums(userId) as ArrayList)

    }
    private fun getJwt():Int{
        val spf = activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)
        val userId = spf!!.getInt("jwt",0)
        return userId
    }

}// end of class