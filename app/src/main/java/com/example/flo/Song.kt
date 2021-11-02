package com.example.flo

data class Song(
    var title : String = "", // 노래 제목
    var singer : String = "", // 가수 이름
    var playTime : Int = 0, // 총 재생시간
    var isPlaying : Boolean = false

)
