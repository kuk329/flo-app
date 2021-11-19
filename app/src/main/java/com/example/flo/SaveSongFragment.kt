package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSaveSongBinding

class SaveSongFragment: Fragment() {

    lateinit var  binding : FragmentSaveSongBinding
    lateinit var songDB: SongDatabase
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        binding = FragmentSaveSongBinding.inflate(inflater,container, false)

        songDB = SongDatabase.getInstance(requireContext())!!


        initRecyclerView()



        return binding.root
    }// end of onCreateView

    override fun onStart() {
        super.onStart()
    //   initRecyclerView()
    }

    private fun initRecyclerView(){
        binding.saveSongRecyclerview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        val saveSongAdapter = SaveSongRvAdapter()

        saveSongAdapter.setMyItemClickListener(object : SaveSongRvAdapter.MyItemClickListener{
            override fun onRemoveSong(songId: Int) {
                Log.d("ttt","삭제될 노래 id: "+songId.toString())
                songDB.songDao().updateIsLikeById(false,songId)
            }
        })
        binding.saveSongRecyclerview.adapter = saveSongAdapter

        saveSongAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList)
        Log.d("ttt",songDB.songDao().getLikedSongs(true).toString())

    }

}// end of class