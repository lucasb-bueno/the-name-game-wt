package com.lucasbueno.thenamegamewt.features.game.presentation.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lucasbueno.thenamegamewt.R
import com.lucasbueno.thenamegamewt.databinding.FragmentGameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameScreenFragment : Fragment(R.layout.fragment_game) {

    private var _binding: FragmentGameBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGameBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}