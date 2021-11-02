package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentHomeBg2Binding

class HomeBackground2Fragment:Fragment() {

    lateinit var binding: FragmentHomeBg2Binding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentHomeBg2Binding.inflate(inflater,container,false)


        return binding.root
    }
}