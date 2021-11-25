package com.example.flo

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding
import com.google.gson.Gson

class SongActivity : AppCompatActivity() {

    // 전역 변수 선언
    lateinit var binding: ActivitySongBinding
    private val context = this
    private var songs = ArrayList<Song>()
    private lateinit var timer: Timer    // 스레드 클래스

    private var mediaPlayer : MediaPlayer?=null // 노래 재생을 위한 MediaPlayer 변수 선언

    private var nowPos = 0

    private lateinit var songDB: SongDatabase

    // Gson
    private var gson : Gson = Gson()

    private var track = 0  // 기본은 한곡만 재생.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
    //    Toast.makeText(this,"SongActivity onCreate()",Toast.LENGTH_SHORT).show()
        Log.d("log","SongActivity onCreate()")


        initPlayList() // DB에서 가져온 데이터 songs 배열에 저장.

        initClickListener()
        seekBarListener()



        // seekBar 움직였을때
        binding.songPlayerSb.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) { // 프로그래스바 변경을 시작하면 일단 중지
                timer.interrupt() // 스레드 중지
                mediaPlayer?.pause() // 미디어 플레이어 일시중지 (다시 시작하려면 .start())
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { // 변경하고 나면
                var positionNow = (seekBar?.progress)?.times(songs[nowPos].playTime)?.div(1000)!!

                // 화면 처리
               // binding.songPlayerSb.progress = positionNow*1000/songs[nowPos].playTime
                binding.songTimeStartTv.text  = String.format("%02d:%02d",positionNow/60,positionNow%60)
                mediaPlayer?.seekTo(positionNow!!*1000) // 음악은 움직인 곳부터 실행
                mediaPlayer?.start() // 노래는 계속해서 실행
                startTimer(positionNow!!) // 타이머도 새로 실행

                if(!songs[nowPos].isPlaying){ //노래가 정지상태 였다면
                    timer.isPlaying = false
                    mediaPlayer?.pause()

                }
            }

        })



    }// end of onCreate

    override fun onStart() { // songActivity 화면을 나갔다가 다시 들어와도 기존 재생된던 값이 남아있도록
        super.onStart()
    //    Toast.makeText(this,"SongActivity onStart()",Toast.LENGTH_SHORT).show()
        Log.d("log","SongActivity onStart()")
        initSong()

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

        songs[nowPos].second = (songs[nowPos].playTime*binding.songPlayerSb.progress)/1000 // progress 구현식 반대로
//        songs[nowPos].isPlaying = false // 해당 노래에 대한 isPlaying 값은 false로
//        setPlayerStatus(false) // 정지가 되었으므로 재생 이미지로 변환

        // sharedPreferences
        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPreferences.edit() // sharedPreferences 조작할때 사용을 한다.

        editor.putInt("songId",songs[nowPos].id) // 현재 실행중인 곡의 아이디 넘김
        editor.putInt("second",songs[nowPos].second+1) // 현재 실행중인 곡의 실행 지점 넘김
        editor.putBoolean("isPlaying",songs[nowPos].isPlaying) // 현재 실행중인 곡의 실행 여부
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


    private fun setPlayer(song:Song){ // 해당 노래 데이터 렌더링 처리 & mediaPlayer 정보 초기화
        val music = resources.getIdentifier(song.music,"raw",this.packageName)

        // 데이터 렌더링
        binding.songTitleTv.text = song.title // 제목
        binding.songSingerTv.text = song.singer // 가수
        binding.songTimeStartTv.text =
            String.format("%02d:%02d",song.second/60,song.second%60)  // 진행 시간
        binding.songTimeEndTv.text =
            String.format("%02d:%02d",song.playTime/60,song.playTime%60) // 노래 총 시간
        binding.songIv.setImageResource(song.coverImg!!) // 노래 이미지
        binding.songLyricsTv.text = song.lyrics

        setPlayerStatus(song.isPlaying)
//
        if(song.isLike){ // 좋아요 렌더링
            binding.songMyLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.songMyLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

        mediaPlayer = MediaPlayer.create(this,music)

    }//end of setPlayer()

    private fun initClickListener(){

        binding.songDownIb.setOnClickListener {
            finish() // 액티비티 종료
        }

        binding.songPlayIv.setOnClickListener {
            setPlayerStatus(true) // 음악 실행
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false) // 음악 중지
        }

        binding.songPrevIv.setOnClickListener {
            moveSong(-1) // 이전곡
        }
        binding.songNextIv.setOnClickListener {
            moveSong(1) // 다음곡
        }

        binding.songMyLikeIv.setOnClickListener {
            setLike(songs[nowPos].isLike)
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
            track = 2 // 다음곡 재생
        }
        binding.songRepeatOnIv.setOnClickListener {
            binding.songRepeatOnIv.visibility = View.GONE
            binding.songRepeatOneIv.visibility = View.VISIBLE
            CustomToast.createToast(context,"현재 음악을 반복합니다.")?.show()
            track = 1 // 현재곡 반복
        }

        binding.songRepeatOneIv.setOnClickListener {
            binding.songRepeatOneIv.visibility = View.GONE
            binding.songRepeatNoneIv.visibility = View.VISIBLE
            CustomToast.createToast(context,"반복을 사용하지 않습니다.")?.show()
            track = 0 // 한곡만 재생
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

    }// end of initClickListener

    private fun seekBarListener(){

    }

    private fun moveSong(direct:Int){

        if(nowPos + direct<0){
            Toast.makeText(this,"첫번째 곡 입니다.",Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos + direct >=songs.size){
            Toast.makeText(this,"마지막 곡 입니다.",Toast.LENGTH_SHORT).show()
            return
        }
        //songs[nowPos].isPlaying = false

        binding.songPlayerSb.progress = 0
        binding.songTimeStartTv.text  = String.format("%02d:%02d",0,0)

        nowPos+=direct
        timer.interrupt() // (프로그래스바)스레드 중지
        startTimer(0) // 현재 곡에 대한 정보로 스레드 재시작

        mediaPlayer?.release() // 미디어 플레이어가 가지고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
        setPlayer(songs[nowPos]) // 노래 정보 바꿈
        setPlayerStatus(true)
    }


    private fun initPlayList(){ // SongActivity에서 사용하는 songs 배열에 곡들을 저장하기 위한 메서드
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs()) // db에서 가져온 데이터들을 songs 배열에 넣음
    }

    private fun startTimer(startPos:Int){ // 현재 노래 정보를 넣어 Timer 를 다시 시작해주는 함수
        timer = Timer(songs[nowPos].playTime,songs[nowPos].isPlaying,startPos)
        timer.start()
    }

    private fun initSong(){ // 처음 노래에 대한 정보 받아와서 처리
        val spf = getSharedPreferences("song", MODE_PRIVATE) // sharedPreference에서 저장된 노래 id 가져옴
        val songId = spf.getInt("songId",0) // songId 가져옴
        val second = spf.getInt("second",0) // 실행된 시간 가져옴
        val isPlaying  = spf.getBoolean("isPlaying",false)

        nowPos = getPlayingSongPosition(songId) // 해당 곡에 대한 songs[]배열에서의 index위치 가져옴.
        binding.songPlayerSb.progress = second*1000/songs[nowPos].playTime
        binding.songTimeStartTv.text  = String.format("%02d:%02d",second/60,second%60)
        songs[nowPos].isPlaying = isPlaying
        songs[nowPos].second = second
        //Log.d("now Song ID",songs[nowPos].id.toString())

        startTimer(second) // 스레드에대한 함수 -> 매개변수로 노래의 실행지점 받아옴.
        timer.isPlaying = false
        setPlayer(songs[nowPos]) // 미디어 처리 함수에 현재 곡에대한 정보 넘김
        mediaPlayer?.seekTo(second*1000)

        if(isPlaying){
            setPlayerStatus(true)
        }



    }// end of initSong()

    private fun getPlayingSongPosition(songId:Int):Int{
        for(i in 0 until songs.size){
            if(songs[i].id == songId){ // songs 리스트에 있는 곡중에 일치하는 id가 있으면 return (sons 배열의 index와(0부터 시작) songs.id 값(1부터 시작)은 다른거 주의)
                return i
            }
        }
        return 0
    }

    private fun setLike(isLike:Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike,songs[nowPos].id)

        if(isLike){ // 좋아요 X
            binding.songMyLikeIv.setImageResource(R.drawable.ic_my_like_off)
            CustomToast.createToast(context,"좋아요 한 곡이 취소되었습니다.")?.show()

           // Toast.makeText(this,"좋아요 한 곡에 담겼습니다.",Toast.LENGTH_SHORT).show()
        }else{ // 좋아요 0
            binding.songMyLikeIv.setImageResource(R.drawable.ic_my_like_on)
            CustomToast.createToast(context,"좋아요 한 곡에 담겼습니다.")?.show()
//            Toast.makeText(this,"좋아요 한 곡이 취소되었습니다.",Toast.LENGTH_SHORT).show()
        }
    }
    fun setPlayerStatus(isPlaying: Boolean){ // 현재 재생 여부 (isPlaying 값) 에 따라 정지와 실행 이미지가 바뀌도록 하는 함수.
        if(!timer.isAlive){
            startTimer(0)
            val music = resources.getIdentifier(songs[nowPos].music,"raw",this.packageName)
            mediaPlayer = MediaPlayer.create(this,music)
        }
        timer.isPlaying = isPlaying // timer 실행여부 변경
        songs[nowPos].isPlaying = isPlaying // 현재 노래의 실행 여부 저장

        if(isPlaying){ // true
            binding.songPlayIv.visibility = View.GONE // 재생 이미지 사라짐
            binding.songPauseIv.visibility = View.VISIBLE // 정지 이미지 보임

            mediaPlayer?.start()
        }else{ // false 노래 멈춤
            binding.songPlayIv.visibility = View.VISIBLE // 재생 이미지 보임
            binding.songPauseIv.visibility = View.GONE // 정지 이미지 사라짐

            mediaPlayer?.pause()
        }

    }

    // Timer는 start 할때만 실행됨.
    inner class Timer(private val playTime:Int, var isPlaying: Boolean,private var second:Int) : Thread(){

        //private var second = 0 //  처음 시간
        override fun run() {
            try {
                while(true){
                    if(second >= playTime){ // 노래 한곡 끝나면
                        second = 0 //  초기화
                        runOnUiThread { // 끝
                            setPlayerStatus(false) // 노래 중지
                            binding.songTimeStartTv.text  = String.format("%02d:%02d",second/60,second%60)
                        }
                        sleep(2000) // 2초후
                        when(track){
                            1->runOnUiThread{ // 재시작(현재곡 반복)
                                mediaPlayer?.seekTo(0)
                                setPlayerStatus(true) // 노래 실행
                            }
                            2->runOnUiThread{ // 순서대로 실행
                                moveSong(1) // 다음곡으로 바꿈
                                setPlayerStatus(true) // 정지 버튼 이미지로 바뀜
                            }
                            else->{ // 반복 안함. track = 0
                                mediaPlayer?.release()
                                mediaPlayer = null
                                setPlayerStatus(false)
                                break
                            }
                        }
                    }
                    if(isPlaying){ // isPlaying이 true 일때만 실행
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
    }// end of class Timer


}// end of Class