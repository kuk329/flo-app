package com.example.flo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    // 전역 변수
    lateinit var binding : ActivityMainBinding
    // Song
    private var song : Song = Song()
    //Gson
    private var gson : Gson = Gson()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    //    Toast.makeText(this,"MainActivity onCreate()",Toast.LENGTH_SHORT).show()
        Log.d("log","MainActivity onCreate()")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val song = Song("01","IF I","백지영",0,221,false,"music_if_i",false)

       // Log.d("log test",song.title + song.singer)


        binding.mainPlayerLayout.setOnClickListener { //  메인 화면에 미니 플레이어를 눌렀을때 SongActivity로 곡에대한 정보를 넘겨주고 넘어감.
            val intent = Intent(this,SongActivity::class.java)
            intent.putExtra("title",song.title) // 노래 제목
            intent.putExtra("singer",song.singer) // 노래
            intent.putExtra("second",song.second) // 몇초까지 재생되었는지
            intent.putExtra("playTime",song.playTime) // 총 노래 시간
            intent.putExtra("isPlaying",song.isPlaying) // 노래가 재생되고 있었는지의 여부-> 전역 변수로 빼서 재생 중인지 아닌지 판단
            intent.putExtra("music",song.music) // 음악 파일
            startActivity(intent)
            // 어짜피 모두 전역변수로 빼서 클리한 곡의 정보를 넘기도록 바꿔야됨.
        }

        binding.mainMiniplayerPlayIv.setOnClickListener {
            binding.mainMiniplayerPauseIv.visibility = View.VISIBLE
            binding.mainMiniplayerPlayIv.visibility=View.GONE
            song.isPlaying = false
        }
        binding.mainMiniplayerPauseIv.setOnClickListener {
            binding.mainMiniplayerPlayIv.visibility = View.VISIBLE
            binding.mainMiniplayerPauseIv.visibility = View.GONE
            song.isPlaying = true
        }

        // bottom Navigation 처리
        initNavigation()

        binding.mainBnv.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }

    }// end of onCreate()


    override fun onStart() { // shardPreference 값 설정
        super.onStart()

     //   Toast.makeText(this,"MainActivity onStart()",Toast.LENGTH_SHORT).show()
        Log.d("log","MainActivity onStart()")
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val jsonSong = sharedPreferences.getString("song",null)

        song = if(jsonSong == null){ // 즉 처음 킨 상태는 저장된 값이 없으므로 그것도 예외처리
            // 여기서는 라일락 노래 에대한 정보를 기본 값으로 초기화
            Song("01","IF I","백지영",0,221,false,"music_if_i",false)

        }else{ // sharedPreference 에 저장된 값을 Song 데이터 클래스 형태로 변환 후

            gson.fromJson(jsonSong,Song::class.java) // Json 형태의 값을 Song 데이터 클래스로 매치
        }
        // 가져온 정보들을 화면에 나타내기 위한 함수
        setMiniPlayer(song)

    }// end of onStart()

    override fun onResume() {
        super.onResume()
      //  Toast.makeText(this,"MainActivity onResume()",Toast.LENGTH_SHORT).show()
        Log.d("log","MainActivity onResume()")

    }// end of onResume()


    override fun onPause() { // 음악에대한 정보 저장등의 내용
        super.onPause()

     //   Toast.makeText(this,"MainActivity onPause()",Toast.LENGTH_SHORT).show()
        Log.d("log","MainActivity onPause()")

        song.second = (binding.mainPlayerSb.progress*song.playTime)/1000 // progress 구한식에서 반대로

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val json = gson.toJson(song)
        editor.putString("song",json) // SongActivity에서 song 전체를 넘겼으므로 여기서도 song 전체를 넘김.
        editor.apply()

    }// end of onPause()

    override fun onStop() {
        super.onStop()
       // Toast.makeText(this,"MainActivity onStop()",Toast.LENGTH_SHORT).show()
        Log.d("log","MainActivity onStop()")
    }// end of onStop()

    override fun onDestroy() {
        super.onDestroy()
     //   Toast.makeText(this,"MainActivity onDestroy()",Toast.LENGTH_SHORT).show()
        Log.d("log","MainActivity onDestroy()")

    }// end of onDestroy()




    private fun initNavigation() {
        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()
    }

    private fun setMiniPlayer(song:Song){ // 미니플레이어 화면 처리
        binding.mainMiniPlayerTitleTv.text = song.title
        binding.mainMiniPlayerSingerTv.text = song.singer
        binding.mainPlayerSb.progress = (song.second*1000/song.playTime)


        if(song.isPlaying){
            binding.mainMiniplayerPauseIv.visibility = View.VISIBLE
            binding.mainMiniplayerPlayIv.visibility = View.GONE
        }else{
            binding.mainMiniplayerPauseIv.visibility = View.GONE
            binding.mainMiniplayerPlayIv.visibility = View.VISIBLE
        }

    }


}// end of Class

