package com.example.flo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    var play :Boolean = false // 노래가 재생 되고있는지 아닌지를 확인 (프로그래스바 와 재생버튼 관련)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra("title")&& intent.hasExtra("singer")){
            binding.songTitleTv.text = intent.getStringExtra("title")
            binding.songSingerTv.text = intent.getStringExtra("singer")

        }

        binding.songDownIb.setOnClickListener {
            finish() // 액티비티 종료
        }

        binding.songPlayIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(true)
        }




//        if(intent.hasExtra("title") && intent.hasExtra("singer")){ // 예외 처리 (값이 안넘어올것을 대비)
//            binding.제목.text = intent.getStringExtra("title")
//            binding.가수.text = intent.getStringExtra("singer")
//
//        }
    }// end of onCreate

    fun setPlayerStatus(isPlaying: Boolean){
        if(isPlaying){ // true
            binding.songPlayIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }else{ // false
            binding.songPlayIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }

    }




}// end of Class