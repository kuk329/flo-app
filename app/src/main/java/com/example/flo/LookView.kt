package com.example.flo

interface LookView { // LookFragment 에서 사용할 interface 정의
    fun onGetSongsLoading()
    fun onGetSongsSuccess(songs:ArrayList<Song>)
    fun onGetSongsFailure(code:Int, message:String)
}