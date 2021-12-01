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


      //  initRecyclerView()

        Log.d("test123","onCreateView call")


        return binding.root
    }// end of onCreateView

    override fun onStart() {
        super.onStart()
        Log.d("test123","onStart call")
       initRecyclerView()
    }

    override fun onPause() {

        super.onPause()
        Log.d("test123","onPause call")
    }

    override fun onStop() {
        super.onStop()
        Log.d("test123","onStop call")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("test123","onDestroyView call")

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

        saveSongAdapter.addSongs(songDB.songDao().getLikedSongs(true) as ArrayList) // songList 추가
        Log.d("ttt",songDB.songDao().getLikedSongs(true).toString())

    }

}// end of class