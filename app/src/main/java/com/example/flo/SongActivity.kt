package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding: ActivitySongBinding
    var play :Boolean = false // 노래가 재생 되고있는지 아닌지를 확인 (프로그래스바 와 재생버튼 관련)
    val context = this

    private val song:Song = Song()
    private lateinit var player: Player
 //   private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initSong()

        player = Player(song.playTime,song.isPlaying)
        player.start()


        binding.songDownIb.setOnClickListener {
            finish() // 액티비티 종료
        }

        binding.songPlayIv.setOnClickListener {
            player.isPlaying = true
            setPlayerStatus(true)
        }

        binding.songPauseIv.setOnClickListener {
            player.isPlaying = false
            setPlayerStatus(false)
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

    private fun initSong(){ // 노래에 대한 정보 받아와서 나타내는 함수
        if(intent.hasExtra("title")&& intent.hasExtra("singer")&&intent.hasExtra("playTime")){
            song.title = intent.getStringExtra("title")!!
            song.singer = intent.getStringExtra("singer")!!
            song.playTime = intent.getIntExtra("playTime",0)!!
            song.isPlaying = intent.getBooleanExtra("isPlaying",false)!!

            binding.songTimeEndTv.text = String.format("%02d:%02d",song.playTime/60,song.playTime%60)
            binding.songTitleTv.text = song.title
            binding.songSingerTv.text = song.singer
            setPlayerStatus(song.isPlaying)
        }


    }

    fun setPlayerStatus(isPlaying: Boolean){
        if(isPlaying){ // true
            binding.songPlayIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
        }else{ // false 노래 멈춤
            binding.songPlayIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
        }

    }

    inner class Player(private val playTime:Int,var isPlaying: Boolean) : Thread(){
        private var second = 0 //  처음 시간

        override fun run() {
            try {
                while(true){
                    if(second >= playTime){
                        break
                    }

                    if(isPlaying){ // true 일때만 실행
                        sleep(1000)
                        second++
                        runOnUiThread{
                            binding.songPlayerSb.progress = second*1000/playTime
                            binding.songTimeStartTv.text  = String.format("%02d:%02d",second/60,second%60)
                        }
                    }
                }
            }catch (e:InterruptedException){
                Log.d("interrupt","스레드가 종료되었습니다.")

            }
        }
    }

    override fun onDestroy() {
        player.interrupt()
        super.onDestroy()
    }




}// end of Class