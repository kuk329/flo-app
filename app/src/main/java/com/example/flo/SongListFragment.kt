package com.example.flo

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.databinding.FragmentSonglistBinding
import com.google.gson.Gson


class SongListFragment:Fragment() {

    lateinit var binding: FragmentSonglistBinding
    private var gson : Gson = Gson()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSonglistBinding.inflate(inflater,container,false)


        val sharedPreferences = this.activity?.getSharedPreferences("songList",
            AppCompatActivity.MODE_PRIVATE
        )
        val jsonSong = sharedPreferences?.getString("songList",null)
     //   val songList:ArrayList<Song> = gson.fromJson(jsonSong,Song::class.java)

      //  val songListItemRvAdapter = SongListItemRvAdapter(songList)
     //   binding.songListItemRecyclerview.adapter = songListItemRvAdapter
      //  binding.songListItemRecyclerview.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

//        binding.songAllSelectTv.setOnClickListener{
//            binding.songAllSelectTv.visibility = View.GONE
//            binding.songAllSelectIv.visibility = View.GONE
//            binding.songAllSelectOffTv.visibility = View.VISIBLE
//            binding.songAllSelectOffIv.visibility = View.VISIBLE
//            binding.songLalacLayout.setBackgroundColor(Color.parseColor("#F3F3F5"))
//            binding.songFluLayout.setBackgroundColor(Color.parseColor("#F3F3F5"))
//            binding.songCoinLayout.setBackgroundColor(Color.parseColor("#F3F3F5"))
//            binding.songByeSpringLayout.setBackgroundColor(Color.parseColor("#F3F3F5"))
//        }
//
//        binding.songAllSelectOffTv.setOnClickListener {
//            binding.songAllSelectOffTv.visibility = View.GONE
//            binding.songAllSelectOffIv.visibility = View.GONE
//            binding.songAllSelectTv.visibility = View.VISIBLE
//            binding.songAllSelectIv.visibility = View.VISIBLE
//            binding.songLalacLayout.setBackgroundColor(Color.TRANSPARENT)
//            binding.songFluLayout.setBackgroundColor(Color.TRANSPARENT)
//            binding.songCoinLayout.setBackgroundColor(Color.TRANSPARENT)
//            binding.songByeSpringLayout.setBackgroundColor(Color.TRANSPARENT)
//
//        }

        return binding.root
    }

    fun newInstance():SongListFragment {
        val args = Bundle()
        val fragment = SongListFragment()
        fragment.arguments = args
        return fragment
    }
}// end of class