package com.example.flo

import com.google.gson.annotations.SerializedName

data class SongResult(
    @SerializedName("songs") val songs:ArrayList<Song>
)

data class SongResponse(
    // LookFragment 에서 응답받는 songs에 관한 데이터 처리
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code :Int,
    @SerializedName("message") val message :String,
    @SerializedName("result") val result :SongResult?

)