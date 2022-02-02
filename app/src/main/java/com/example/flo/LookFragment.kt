package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentLookBinding


class LookFragment : Fragment(), LookView {

    lateinit var binding: FragmentLookBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLookBinding.inflate(inflater, container, false)
        return binding.root
    }

    // LookView interface

    override fun onGetSongsLoading() {
        TODO("Not yet implemented")
    }

    override fun onGetSongsSuccess() {
        TODO("Not yet implemented")
    }

    override fun onGetSongsFailure(code: Int, message: String) {
        TODO("Not yet implemented")
    }


}