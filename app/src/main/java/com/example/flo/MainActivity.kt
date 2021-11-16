package com.example.flo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    // 전역 변수
    lateinit var binding : ActivityMainBinding
    private var song : Song = Song() // Song
    private var gson : Gson = Gson() //Gson

    // 미디어 플레이어
    private var mediaPlayer : MediaPlayer?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //    Toast.makeText(this,"MainActivity onCreate()",Toast.LENGTH_SHORT).show()
        Log.d("log","MainActivity onCreate()")


        initNavigation() // bottom Navigation 처리
        inputDummyAlbums()  // RoomDB에 데이터들 저장
        inputDummySongs() // RoomDB에 데이터들 저장



        binding.mainPlayerLayout.setOnClickListener { //  메인 화면에 미니 플레이어를 눌렀을때 현재 곡에대한 id sharedPreference로 저장.
            Log.d("nowSongID",song.id.toString())
            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
            editor.putInt("songId",song.id)
            editor.apply()

            val intent = Intent(this@MainActivity,SongActivity::class.java)
            startActivity(intent)

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



    }// end of onCreate()


    override fun onStart() { // shardPreference 값 가져옴.
        super.onStart()

     //   Toast.makeText(this,"MainActivity onStart()",Toast.LENGTH_SHORT).show()
        Log.d("log","MainActivity onStart()")

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPreferences.getInt("songId",0) // 진행중이던 곡의 아이디 가져옴. song db의 pk는 id로

        val songDB = SongDatabase.getInstance(this)!!
        song = if(songId==0){ // 실행중인 노래가 없었다면 테이블 첫번째 노래를 넣어줌
            songDB.songDao().getSong(1)
        }else{
            songDB.songDao().getSong(songId)
        }
        Log.d("song ID",song.id.toString())
        setMiniPlayer(song) //미니플레이어 데이터 렌더링

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


    }

    private fun setMiniPlayer(song:Song){ // 미니플레이어 데이터 렌더링 함수
        binding.mainMiniPlayerTitleTv.text = song.title
        binding.mainMiniPlayerSingerTv.text = song.singer
        binding.mainPlayerSb.progress = (song.second*1000/song.playTime)

        val music = resources.getIdentifier(song.music,"raw",this.packageName)


        if(song.isPlaying){
            binding.mainMiniplayerPauseIv.visibility = View.VISIBLE
            binding.mainMiniplayerPlayIv.visibility = View.GONE
        }else{
            binding.mainMiniplayerPauseIv.visibility = View.GONE
            binding.mainMiniplayerPlayIv.visibility = View.VISIBLE
        }
    }

    // ROOM DB
    private fun inputDummyAlbums(){
        val songDB = SongDatabase.getInstance(this)!!
        val albums = songDB.albumDao().getAlbums()

        if(albums.isNotEmpty()) return // db가 비어있지 않으면 더미데이터를 채울 필요 없음

        songDB.albumDao().insert(
            Album(
                1,
                "IU 5th Album 'LILAC'", "아이유 (IU)", R.drawable.album_img1,"2021.03.25","정규 | 댄스 팝"

            )
        )
        songDB.albumDao().insert(
            Album(
                2,
                "My Universe", "Coldplay & 방탄소년단", R.drawable.album_img2,"2021.09.24","싱글 | 팝,얼터너티브 팝"
            )
        )

        songDB.albumDao().insert(
            Album(
                3,
                "연모 OST Part.3", "백지영", R.drawable.album_img3,"2021.10.26","OST | TV 드라마"
            )
        )

        songDB.albumDao().insert(
            Album(
                4,
                "서로의 서로", "적재", R.drawable.album_img4,"2021.11.16","싱글 | 록"
            )
        )

        songDB.albumDao().insert(
            Album(
                5,
                "MSG워너비 1집", "MSG워너비", R.drawable.album_img5,"2021.06.26","싱글 | 발라드"
            )
        )

        songDB.albumDao().insert(
            Album(
                6,
                "나의 첫사랑", "다비치", R.drawable.album_img6,"2021.10.18","싱글 | 발라드"
            )
        )

    }

    private fun inputDummySongs(){
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()  // 테이블의 모든 데이터 가져오기

        if(songs.isNotEmpty()) return // 테이블에 값이 아무것도 없으면 함수 종료

        // 더미 데이터
        songDB.songDao().insert(Song("IF I","백지영",0,221,false,"music_if_i",R.drawable.song1_img))
        songDB.songDao().insert(Song("라일락","아이유(IU)",0,214,false,"music_lilac",R.drawable.album_img1))
        songDB.songDao().insert(Song("strawberry moon","아이유(IU)",0,205,false,"music_strawberry_moon",R.drawable.song2_img))
        songDB.songDao().insert(Song("Yours","진",0,264,false,"music_yours",R.drawable.song6_img))
        songDB.songDao().insert(Song("Butter","방탄소년단",0,167,false,"music_butter",R.drawable.song4_img))
        songDB.songDao().insert(Song("Permission to Dance","방탄소년단",0,188,false,"permission_to_dance",R.drawable.song5_img))
        songDB.songDao().insert(Song("나 그댈위해 시 한편을 쓰겠어","케이시",0,219,false,"music_strawberry_moon",R.drawable.song7_img))
        songDB.songDao().insert(Song("너는 내 세상이었어","볼빨간사춘기",0,274,false,"you_are_my_world",R.drawable.song8_img))
        songDB.songDao().insert(Song("나의 첫사랑","다비치",0,236,false,"music_strawberry_moon",R.drawable.song9_img))
        songDB.songDao().insert(Song("My Universe","Coldplay & 방탄소년단",0,228,false,"music_myuniverse",R.drawable.song11_img))

        val _songs= songDB.songDao().getSongs() // 들어간 더미 데이터 값 확인
        Log.d("roomDB",_songs.toString())
    }




}// end of Class

