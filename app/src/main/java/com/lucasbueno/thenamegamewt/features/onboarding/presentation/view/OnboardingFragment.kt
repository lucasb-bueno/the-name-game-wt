package com.lucasbueno.thenamegamewt.features.onboarding.presentation.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lucasbueno.thenamegamewt.R
import com.lucasbueno.thenamegamewt.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOnboardingBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}