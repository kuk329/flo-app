package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private val information = arrayListOf("저장한 곡","음악파일")


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
    }



}