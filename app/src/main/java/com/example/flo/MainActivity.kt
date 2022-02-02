package com.example.flo

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.RoomDatabase
import com.example.flo.databinding.ActivityMainBinding
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    // 전역 변수
    lateinit var binding : ActivityMainBinding
    //private var song : Song = Song() // Song
    private var gson : Gson = Gson() //Gson

    // 미디어 플레이어
    private var mediaPlayer : MediaPlayer?=null
    // Timer
    private lateinit var timer : Timer


    private var songs = ArrayList<Song>()

    private lateinit var songDB : SongDatabase

    private var nowPos = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //    Toast.makeText(this,"MainActivity onCreate()",Toast.LENGTH_SHORT).show()
        Log.d("log","MainActivity onCreate()")


        initNavigation() // bottom Navigation 처리
        inputDummyAlbums()  // RoomDB에 데이터들 저장
        inputDummySongs() // RoomDB에 데이터들 저장
        initPlayList() // songs 배열 초기화
        initClickListener()



        binding.mainPlayerLayout.setOnClickListener { //  메인 화면에 미니 플레이어를 눌렀을때 현재 곡에대한 id sharedPreference로 저장.
//            Log.d("nowSongID",songs[nowPos].id.toString())
//            val editor = getSharedPreferences("song", MODE_PRIVATE).edit()
//            editor.putInt("songId",songs[nowPos].id)
//            editor.apply()

            val intent = Intent(this@MainActivity,SongActivity::class.java)
            startActivity(intent)

        }

        binding.mainMiniplayerPlayIv.setOnClickListener { // 시작 버튼
            binding.mainMiniplayerPauseIv.visibility = View.VISIBLE
            binding.mainMiniplayerPlayIv.visibility=View.GONE
            setMiniPlayerStatus(true)

        }
        binding.mainMiniplayerPauseIv.setOnClickListener {// 중지 버튼
            binding.mainMiniplayerPlayIv.visibility = View.VISIBLE
            binding.mainMiniplayerPauseIv.visibility = View.GONE
            setMiniPlayerStatus(false)

        }



    }// end of onCreate()


    override fun onStart() { // shardPreference 값 가져옴.
        super.onStart()

     //   Toast.makeText(this,"MainActivity onStart()",Toast.LENGTH_SHORT).show()
        Log.d("log","MainActivity onStart()")

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPreferences.getInt("songId",0) // 진행중이던 곡의 아이디 가져옴. song db의 pk는 id로
        val songSecond = sharedPreferences.getInt("second",0) // 진행중이던 곡의 실행 지점 가져옴.
        val isPlaying = sharedPreferences.getBoolean("isPlaying",false)

        nowPos = getPlayingSongPosition(songId)
        songs[nowPos].isPlaying = isPlaying
        songs[nowPos].second = songSecond

//        Log.d("songll ID",songs[nowPos].id.toString())
//        Log.d("songll Second",songSecond.toString())


        setMiniPlayer(songs[nowPos]) //미니플레이어 데이터 렌더링
        binding.mainPlayerSb.progress = songSecond*1000/songs[nowPos].playTime

        timer = Timer(songs[nowPos].playTime,songs[nowPos].isPlaying,songSecond)
        timer.start()
        mediaPlayer?.seekTo(songSecond*1000)

          //startTimer(nowPos)
            if(isPlaying){
                setMiniPlayerStatus(true)
            }



//        val songDB = SongDatabase.getInstance(this)!!
//        songs[nowPos] = if(songId==0){ // 실행중인 노래가 없었다면 테이블 첫번째 노래를 넣어줌
//            songDB.songDao().getSong(1)
//        }else{
//            songDB.songDao().getSong(songId)
//        }


    }// end of onStart()

    private fun getPlayingSongPosition(songId:Int):Int{
        for(i in 0 until songs.size){
            if(songs[i].id==songId){
                return i
            }
        }
        return 0
    }

    override fun onResume() {
        super.onResume()
      //  Toast.makeText(this,"MainActivity onResume()",Toast.LENGTH_SHORT).show()
        Log.d("log","MainActivity onResume()")

    }// end of onResume()


    override fun onPause() { // 음악에대한 정보 저장등의 내용
        super.onPause()

     //   Toast.makeText(this,"MainActivity onPause()",Toast.LENGTH_SHORT).show()
        Log.d("log","MainActivity onPause()")

        songs[nowPos].second = (binding.mainPlayerSb.progress*songs[nowPos].playTime)/1000 // progress 구한식에서 반대로

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putInt("songId",songs[nowPos].id) // 현재 곡 ID
        editor.putInt("second",songs[nowPos].second+1) // 현재 곡 진행률
        editor.putBoolean("isPlaying",songs[nowPos].isPlaying) // 현재 곡 실행 여부
        editor.apply()

        songs[nowPos].isPlaying = false
        timer.interrupt()
        mediaPlayer?.release()
        mediaPlayer = null


    }// end of onPause()

    override fun onStop() {
        super.onStop()
       // Toast.makeText(this,"MainActivity onStop()",Toast.LENGTH_SHORT).show()
        Log.d("log","MainActivity onStop()")
    }// end of onStop()

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt() // 스레드 해제
        mediaPlayer?.release() // 미디어 플레이어가 갖고있던 리소스를 해제
        mediaPlayer = null // 미디어 플레이어 해제

        //   Toast.makeText(this,"MainActivity onDestroy()",Toast.LENGTH_SHORT).show()
        Log.d("log","MainActivity onDestroy()")

    }// end of onDestroy()

    // --------------------------------------  function ---------------------------------------------



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

    }// end of initNavigation

    private fun setMiniPlayer(song:Song){ // 미니플레이어 데이터 렌더링 함수
        binding.mainMiniPlayerTitleTv.text = song.title
        binding.mainMiniPlayerSingerTv.text = song.singer
        binding.mainPlayerSb.progress = (song.second*1000/song.playTime)

        val music = resources.getIdentifier(song.music,"raw",this.packageName)


        if(song.isPlaying){
            binding.mainMiniplayerPauseIv.visibility = View.VISIBLE
            binding.mainMiniplayerPlayIv.visibility = View.GONE
            //start
        }else{
            binding.mainMiniplayerPauseIv.visibility = View.GONE
            binding.mainMiniplayerPlayIv.visibility = View.VISIBLE
        }

        mediaPlayer = MediaPlayer.create(this,music)



    }// end of setMiniPlayer

    private fun setMiniPlayerStatus(isPlaying:Boolean){
        timer.isPlaying = isPlaying
        songs[nowPos].isPlaying = isPlaying
        if(isPlaying){
            binding.mainMiniplayerPauseIv.visibility = View.VISIBLE
            binding.mainMiniplayerPlayIv.visibility = View.GONE
            mediaPlayer?.start() // 음악 실행


        }else{
            binding.mainMiniplayerPauseIv.visibility = View.GONE
            binding.mainMiniplayerPlayIv.visibility = View.VISIBLE
            mediaPlayer?.pause() // 음악 중지


        }

    }

    private fun initClickListener(){
        binding.mainMiniPlayerNext.setOnClickListener {  // 다음 곡
            moveSong(1)

        }
        binding.mainMiniPlayerPrev.setOnClickListener { // 이전 곡
            moveSong(-1)

        }
    }

    private fun moveSong(direct:Int){

        if(nowPos+direct<0){
            Toast.makeText(this,"첫번째 곡 입니다.",Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos+direct>=songs.size){
            Toast.makeText(this,"마지막 곡 입니다.",Toast.LENGTH_SHORT).show()
            return
        }

        binding.mainPlayerSb.progress = 0


        nowPos+=direct // 현재 노래 index 변경

        timer.interrupt()
        startTimer(0)
        mediaPlayer?.release()
        mediaPlayer = null
        setMiniPlayer(songs[nowPos]) // 노래 바꿈

        binding.mainMiniplayerPauseIv.visibility = View.GONE
        binding.mainMiniplayerPlayIv.visibility = View.VISIBLE

        Handler(Looper.getMainLooper()).postDelayed({
            setMiniPlayerStatus(true)
        },500) // 1초가 1000mills



    }
    private fun startTimer(startPos:Int){ // 현재 노래 정보를 넣어 Timer 를 다시 시작해주는 함수
        timer = Timer(songs[nowPos].playTime,songs[nowPos].isPlaying,startPos)
        timer.start()
    }


    // Thread
    inner class Timer(private val playTime:Int ,var isPlaying:Boolean,private var second:Int):Thread(){

        override fun run() {
            try{
                while(true){
                    if(second>playTime){
                        break
                    }
                    if(isPlaying){
                        sleep(1000)
                        second++
                        runOnUiThread {
                            binding.mainPlayerSb.progress = second*1000/playTime

                        }
                    }

                }

            }catch (e:InterruptedException){
                Log.d("interrupt","스레드가 종료되었습니다.")
            }

        }

    }// end of Timer class




    private fun initPlayList(){ // 미니 플레이어 플레이 리스트 초기화
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
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
        songDB.songDao().insert(Song("IF I","백지영",0,221,false,"music_if_i",R.drawable.song1_img,"","IF I.. IF I.. 사랑이면 이제 난 어떡해야 해"))
        songDB.songDao().insert(Song("라일락","아이유(IU)",0,214,false,"music_lilac",R.drawable.album_img1,"","날리는 꽃가루에 눈이 따금해(아야)"))
        songDB.songDao().insert(Song("strawberry moon","아이유(IU)",0,205,false,"music_strawberry_moon",R.drawable.song2_img,"","달이 익어가니 서둘러 젊은 피야"))
        songDB.songDao().insert(Song("Yours","진",0,264,false,"music_yours",R.drawable.song6_img,"깊어진 하루 길어진 내 그림자 저멀리"))
        songDB.songDao().insert(Song("Butter","방탄소년단",0,167,false,"music_butter",R.drawable.song4_img,"","Smooth like butter, like a criminal undercover"))
        songDB.songDao().insert(Song("Permission to Dance","방탄소년단",0,188,false,"permission_to_dance",R.drawable.song5_img,"","It's the thought of being young "))
        songDB.songDao().insert(Song("나 그댈위해 시 한편을 쓰겠어","케이시",0,219,false,"music_strawberry_moon",R.drawable.song7_img,"","나 그댈 위해 시 한편을 쓰겠어"))
        songDB.songDao().insert(Song("너는 내 세상이었어","볼빨간사춘기",0,274,false,"you_are_my_world",R.drawable.song8_img,"","오늘만 같이 있게 해 줘 마지막이란 게"))
        songDB.songDao().insert(Song("나의 첫사랑","다비치",0,236,false,"music_strawberry_moon",R.drawable.song9_img,"","그때의 나는 너를 만나서 하나부터 열까지"))
        songDB.songDao().insert(Song("My Universe","Coldplay & 방탄소년단",0,228,false,"music_myuniverse",R.drawable.song11_img,"","You(you),you are(you are) my universe"))

        val _songs= songDB.songDao().getSongs() // 들어간 더미 데이터 값 확인
        Log.d("roomDB",_songs.toString())
    }




}// end of Class

