package com.example.flo

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// 이 클래스를 사용하면 song 목록에 대한 정보들을 받아올수 있음.

class SongService {
    private lateinit var lookView : LookView
    // 기본 서버 주소
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://13.125.121.202")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // 외부 접근
    fun setLookView(lookView:LookView){
        this.lookView = lookView
    }

    fun getSongs(){
        val songService =retrofit.create(SongRetrofitInterface::class.java)

        lookView.onGetSongsLoading() // api 호출 직전에 loading 함수 호출

        songService.getSongs().enqueue(object : Callback<SongResponse> {
            override fun onResponse(call: Call<SongResponse>, response: Response<SongResponse>) { // 제대로 응답을 받았을때
                Log.d("LookFragment/api",response.toString())

                val resp = response.body()!!

//                Log.d("LookFragment/api",resp.toString())
//                Log.d("LookFragment/api",resp.message.toString())

                when(resp.code){ // 서버로 부터 받아온 code 값이 1000 이면 성공 아니면 실패
                    1000 -> lookView.onGetSongsSuccess(resp.result!!.songs)
                    else-> lookView.onGetSongsFailure(resp.code, resp.message)

                }
            }

            override fun onFailure(call: Call<SongResponse>, t: Throwable) { // 네트워크 응답 실패시
                Log.d("LookFragment/api",t.message.toString())
                lookView.onGetSongsFailure(400,"네트워크 오류가 발생했습니다.")
            }
        })
    }// end of signUp()




}