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
    //private var albumDatas = ArrayList<Album>()
    lateinit var songDB: SongDatabase
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        // layoutManager 설정
        binding.saveSongRecyclerview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)



        return binding.root
    }// end of onCreateView

    override fun onStart() {
        super.onStart()
        initRecyclerView()
    }

    private fun initRecyclerView(){
        binding.saveSongRecyclerview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)

        val saveSongAdapter = SaveSongRvAdapter()

    }

}// end of class