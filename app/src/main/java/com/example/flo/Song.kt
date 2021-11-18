package com.example.flo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class Song(
   // var order : Int?= null, // (앨범 안에) 곡 순서
    var title: String = "",
    var singer: String = "",
    var second: Int = 0, // 노래 진행된 시간
    var playTime: Int = 0, // 총 재생 시간
    var isPlaying: Boolean = false, // 실행 여부
    var music: String = "", // 음악 파일명
    var coverImg: Int? = null, // 이미지 소스
    var lyrics : String = "", // 가사
    var isLike: Boolean = false, // 좋아요 여부
    var albumIdx: Int = 0 // 이 song이 어떤 앨범에 담겨 있는지 가리키는 변수 (foreign key 역할)
){
    @PrimaryKey(autoGenerate = true) var id:Int = 0
}
