package com.example.flo

data class Song(
    var order : String="", // (앨범 안에) 곡 순서
    var title : String = "", // 노래 제목
    var singer : String = "", // 가수 이름
    var second : Int= 0, // 몇초까지 재생되었는지
    var playTime : Int = 0, // 총 재생시간
    var isPlaying : Boolean = false,
    var music: String = "", // 음악 파일
    var titleSong : Boolean = false // 타이틀 곡인지 확인

)
