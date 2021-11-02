package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentSaveMusicFileBinding

class SaveMusicFileFragment:Fragment() {

    lateinit var binding:FragmentSaveMusicFileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {

        binding = FragmentSaveMusicFileBinding.inflate(inflater,container,false)

        return binding.root
    }
}