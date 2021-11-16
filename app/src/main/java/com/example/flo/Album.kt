package com.example.flo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "AlbumTable")
data class Album(

    @PrimaryKey(autoGenerate = false) var id:Int=0, // album의 pk는 임의로 지정해주기 위해 autogenerate를 안씀.
    var title : String? = "",
    var singer : String? = "",
    var coverImg : Int? = null,
    var releaseDate : String? = "",
    var genre : String? = "",


)

// 곡 제목
// 가수 명
// 커버 이미지
// 수록곡
