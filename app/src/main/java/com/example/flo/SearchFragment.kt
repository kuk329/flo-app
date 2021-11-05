package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding

    var total = 0 // 전체 시간을 저장하는 변수
    var started = false // 시작됬는지를 체크하기 위한 변수수

   override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

       val handler = object : Handler(Looper.getMainLooper()){
           override fun handleMessage(msg: Message) {
               val minute = String.format("%02d",total/60)
           }
       }


        return binding.root
    }


}