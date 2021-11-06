package com.example.flo

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    // 전역 변수 선언
    lateinit var binding: ActivitySongBinding
  //  var play :Boolean = false // 노래가 재생 되고있는지 아닌지를 확인 (프로그래스바 와 재생버튼 관련)
    private val context = this

    private var song:Song = Song()
    private lateinit var timer: Timer // 스레드 클래스 선언

    private var mediaPlayer : MediaPlayer?=null // 노래 재생을 위한 MediaPlayer 변수 선언

    // Gson
    private var gson : Gson = Gson()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
    //    Toast.makeText(this,"SongActivity onCreate()",Toast.LENGTH_SHORT).show()
        Log.d("log","SongActivity onCreate()")

        initSong() // MainActivity 에서 가져온 값을 song 데이터 클래스 객체에 저장

//        timer = Timer(song.playTime,song.isPlaying) // timer 에대한 매개변수로 위에서 데이터가 채워진 song 객체 넘김
//        timer.start() // 스레드 시작!


        binding.songDownIb.setOnClickListener {
            finish() // 액티비티 종료
        }

        binding.songPlayIv.setOnClickListener { // 재생버튼 눌렀을때
            setPlayerStatus(true)
            timer.isPlaying = true
            song.isPlaying = true
            mediaPlayer?.start() // 미디어 플레이어 실행
        }

        binding.songPauseIv.setOnClickListener { // 정지버튼 눌렀을때
            setPlayerStatus(false)
            timer.isPlaying = false
            song.isPlaying=false
            mediaPlayer?.pause() // 미디어 플레이어 정지
        }

        // 좋아요 버튼
        binding.songMyLikeOffBtn.setOnClickListener {
            binding.songMyLikeOffBtn.visibility = View.GONE
            binding.songMyLikeOnBtn.visibility = View.VISIBLE
            CustomToast.createToast(context,"좋아요 한 곡에 담겼습니다.")?.show()
           // Toast.makeText(this,"좋아요 한 곡에 담겼습니다.",Toast.LENGTH_SHORT).show()

        }

        binding.songMyLikeOnBtn.setOnClickListener {
            binding.songMyLikeOnBtn.visibility = View.GONE
            binding.songMyLikeOffBtn.visibility = View.VISIBLE
            CustomToast.createToast(context,"좋아요 한 곡이 취소되었습니다.")?.show()
//            Toast.makeText(this,"좋아요 한 곡이 취소되었습니다.",Toast.LENGTH_SHORT).show()

        }
        // 랜덤 재생 버튼
        binding.songPlayRandomOff.setOnClickListener {
            binding.songPlayRandomOff.visibility = View.GONE
            binding.songPlayRandomOn.visibility = View.VISIBLE
            CustomToast.createToast(context,"재생목록을 랜덤으로 재생합니다.")?.show()

        }

        binding.songPlayRandomOn.setOnClickListener {
            binding.songPlayRandomOn.visibility = View.GONE
            binding.songPlayRandomOff.visibility = View.VISIBLE
            CustomToast.createToast(context,"재생목록을 순서대로 재생합니다.")?.show()

        }


        // 반복 재생 버튼
        binding.songRepeatNoneIv.setOnClickListener {
            binding.songRepeatNoneIv.visibility = View.GONE
            binding.songRepeatOnIv.visibility = View.VISIBLE
            CustomToast.createToast(context,"전체 음악을 반복합니다.")?.show()
        }
        binding.songRepeatOnIv.setOnClickListener {
            binding.songRepeatOnIv.visibility = View.GONE
            binding.songRepeatOneIv.visibility = View.VISIBLE
            CustomToast.createToast(context,"현재 음악을 반복합니다.")?.show()
        }

        binding.songRepeatOneIv.setOnClickListener {
            binding.songRepeatOneIv.visibility = View.GONE
            binding.songRepeatNoneIv.visibility = View.VISIBLE
            CustomToast.createToast(context,"반복을 사용하지 않습니다.")?.show()
        }

        // 곡 제외 버튼
        binding.songUnlikeOffIb.setOnClickListener {
            binding.songUnlikeOffIb.visibility = View.GONE
            binding.songUnlikeOnIb.visibility = View.VISIBLE
        }
        binding.songUnlikeOnIb.setOnClickListener {
            binding.songUnlikeOnIb.visibility = View.GONE
            binding.songUnlikeOffIb.visibility = View.VISIBLE
        }

    }// end of onCreate

    override fun onStart() { // songActivity 화면을 나갔다가 다시 들어와도 기존 재생된던 값이 남아있도록
        super.onStart()
    //    Toast.makeText(this,"SongActivity onStart()",Toast.LENGTH_SHORT).show()
        Log.d("log","SongActivity onStart()")


        // 기존에 틀고있던 노래가 있으면 그 정보를 가져오기 위한것.
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val jsonSong = sharedPreferences.getString("song",null)

        if(jsonSong!=null){
            song = gson.fromJson(jsonSong,Song::class.java)
            timer = Timer(song.playTime,song.isPlaying,song.second)
            timer.start()
        }else{
            timer = Timer(song.playTime,song.isPlaying,song.second) 
           timer.start() // 스레드 시작!
        }



//        if(songSecond==0){ // 기존에 노래가 재생된 시간값이 없으면
//            timer = Timer(song.playTime,song.isPlaying,0) // timer 에대한 매개변수로 위에서 데이터가 채워진 song 객체 넘김
//            timer.start() // 스레드 시작!
//
//        }else{
//            timer = Timer(song.playTime,song.isPlaying,songSecond)
//            timer.start()
//        }
    }

    override fun onResume() {
        super.onResume()

      //  Toast.makeText(this,"SongActivity onResume()",Toast.LENGTH_SHORT).show()
        Log.d("log","SongActivity onResume()")
    }


    override fun onPause() {
        super.onPause()
    //    Toast.makeText(this,"SongActivity onPause()",Toast.LENGTH_SHORT).show()
        Log.d("log","SongActivity onPause()")

        mediaPlayer?.pause() // 미디어 플레이어 중지
        timer.isPlaying = false// 스레드 중지
        song.isPlaying = false // 노래 실행 여부도 정지로 바꿈
        song.second = (binding.songPlayerSb.progress*song.playTime)/1000 // progress 구한식에서 반대로
        setPlayerStatus(false) // 정지가 되었으므로 재생 이미지로 변환


        // sharedPreferences
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() // sharedPreferences 조작할때 사용을 한다.

        // 노가다 코드
//        editor.putString("title",song.title)
//        editor.putString("singer",song.singer)
//        editor.putBoolean("isPlaying",song.isPlaying)



        // Gson
        val json = gson.toJson(song) // Gson을 사용해 song data 객체를 Json으로 변환
        editor.putString("song",json)

        editor.apply()

    }// end of onPause()


    override fun onStop() {
        super.onStop()
     //   Toast.makeText(this,"SongActivity onStop()",Toast.LENGTH_SHORT).show()
        Log.d("log","SongActivity onStop()")

    }// end of onStop()


    override fun onDestroy() { // 대부분의 리소스 해제 작업
   //     Toast.makeText(this,"SongActivity onDestroy()",Toast.LENGTH_SHORT).show()
        Log.d("log","SongActivity onDestroy()")
        super.onDestroy()
        timer.interrupt() // 스레드 해제
        mediaPlayer?.release() // 미디어 플레이어가 갖고있던 리소스를 해제
        mediaPlayer = null // 미디어 플레이어 해제

    }// end of onDestroy()




    // ------------------------------- 함수 -----------------------------------------




    private fun initSong(){ // 노래에 대한 정보 받아와서 나타내는 함수
        if(intent.hasExtra("title")&& intent.hasExtra("singer")&& intent.hasExtra("second") &&intent.hasExtra("playTime")
            &&intent.hasExtra("isPlaying") &&intent.hasExtra("music")){
            song.title = intent.getStringExtra("title")!! // 제목
            song.singer = intent.getStringExtra("singer")!! // 가수 이름
            song.second = intent.getIntExtra("second",0) // 몇초까지 재생되었는지
            song.playTime = intent.getIntExtra("playTime",0)!! // 총 재생 시간
            song.isPlaying = intent.getBooleanExtra("isPlaying",false)!! // 재생 여부
            song.music = intent.getStringExtra("music")!! // 음악 파일(노래 일므)

            // 노래 이름만으로는 노래를 가져올수 없으므로 resources.getIdentifier(노래 이름,"raw",this.packageName)
            val music = resources.getIdentifier(song.music,"raw",this.packageName)

            binding.songTimeEndTv.text = String.format("%02d:%02d",song.playTime/60,song.playTime%60)
            binding.songTitleTv.text = song.title
            binding.songSingerTv.text = song.singer
            setPlayerStatus(song.isPlaying)
            mediaPlayer = MediaPlayer.create(this,music) // 미디어 플레이어에게 노래 파일 정보 넘겨주기.
        }


    }

    fun setPlayerStatus(isPlaying: Boolean){ // 현재 재생 여부 (isPlaying 값) 에 따라 정지와 실행 이미지가 바뀌도록 하는 함수.
        if(isPlaying){ // true
            binding.songPlayIv.visibility = View.GONE // 재생 이미지 사라짐
            binding.songPauseIv.visibility = View.VISIBLE // 정지 이미지 보임
        }else{ // false 노래 멈춤
            binding.songPlayIv.visibility = View.VISIBLE // 재생 이미지 보임
            binding.songPauseIv.visibility = View.GONE // 정지 이미지 사라짐
        }

    }

    inner class Timer(private val playTime:Int, var isPlaying: Boolean,var second:Int) : Thread(){
       // private var second = 0 //  처음 시간

        override fun run() {
            try {
                while(true){
                    if(second >= playTime){
                        break
                    }
                    if(isPlaying){ // true 일때만 실행
                        sleep(1000) // 진행중인 변수를 1초마다 증가하도록 (즉 1초마다 변하도록)
                        second++
                        runOnUiThread{ // ui를 건드릴수 있게 하기 위해
                            binding.songPlayerSb.progress = second*1000/playTime
                            binding.songTimeStartTv.text  = String.format("%02d:%02d",second/60,second%60)
                        }
                    }
                } // end of while
            }catch (e:InterruptedException){
                Log.d("interrupt","스레드가 종료되었습니다.")

            }
        }
    }



}// end of Class