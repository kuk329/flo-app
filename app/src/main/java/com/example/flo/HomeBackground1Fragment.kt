package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHomeBg1Binding

class HomeBackground1Fragment:Fragment() {

    lateinit var binding: FragmentHomeBg1Binding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentHomeBg1Binding.inflate(inflater,container,false)


        return binding.root
    }
}