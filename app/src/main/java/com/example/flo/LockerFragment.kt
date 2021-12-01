package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private val information = arrayListOf("저장한 곡","음악파일","저장앨범")


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)


        val adapter =  LockerViewPageAdapter(this)
        binding.lockerVp.adapter = adapter

        //TabLayout 과 ViewPage 연결
        TabLayoutMediator(binding.lockerTb,binding.lockerVp){
            tab,position->
            tab.text=information[position]
        }.attach()

        return binding.root

    } // end of onCreateView

    override fun onStart() {
        super.onStart()

        initView()
    }

    private fun initView(){ // 로그인이 되어있을떄 아닐때 화면 처리
        val jwt = getJwt()

        if(jwt==0){
            binding.lockerLoginTv.text = "로그인"
            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(activity,LoginActivity::class.java))
            }
       }else{
            binding.lockerLoginTv.text="로그아웃"
            binding.lockerLoginTv.setOnClickListener {

                logout() // 로그아웃 시켜주는 함수수
               startActivity(Intent(activity,MainActivity::class.java))
            }
        }

    }

    private fun getJwt():Int{
        val spf = activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)

        return spf!!.getInt("jwt",0)
    }

    private fun logout(){
        val spf  = activity?.getSharedPreferences("auth",AppCompatActivity.MODE_PRIVATE)

        val editor = spf!!.edit()
        editor.remove("jwt")
        editor.apply()
    }// logout()


}