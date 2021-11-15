package com.example.flo

data class Album(
    var title : String? = "",
    var singer : String? = "",
    var coverImg : Int? = null,
    var songs : ArrayList<Song>?=null


)

// 곡 제목
// 가수 명
// 커버 이미지
// 수록곡
