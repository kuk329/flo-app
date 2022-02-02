package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentLookBinding


class LookFragment : Fragment(), LookView {

    private lateinit var binding: FragmentLookBinding
    private lateinit var songRvAdapter: SongRvAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initRecyclerView()
        getSongs()
    }

    private fun initRecyclerView(){
        // layoutManager 설정은 xml 파일에서 처리함.
        songRvAdapter = SongRvAdapter(requireContext())
        binding.lookFragmentRecyclerView.adapter = songRvAdapter
    }

    private fun getSongs(){
        val songService = SongService()

        songService.setLookView(this)

        songService.getSongs()


    }



    // LookView interface

    override fun onGetSongsLoading() {
        binding.fragmentLookProgressBar.visibility = View.VISIBLE
    }

    override fun onGetSongsSuccess(songs:ArrayList<Song>) {
        binding.fragmentLookProgressBar.visibility = View.GONE

        songRvAdapter.addSongs(songs)
    }

    override fun onGetSongsFailure(code: Int, message: String) {
        binding.fragmentLookProgressBar.visibility = View.GONE

        when(code){
            400 -> Log.d("LookFrag/API-ERROR",message)

        }
    }


}