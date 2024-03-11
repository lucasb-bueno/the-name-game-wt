package com.lucasbueno.thenamegamewt.features.onboarding.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lucasbueno.thenamegamewt.R
import com.lucasbueno.thenamegamewt.databinding.FragmentOnboardingBinding
import com.lucasbueno.thenamegamewt.features.onboarding.presentation.viewmodel.OnboardingViewModel
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lucasbueno.thenamegamewt.features.game.domain.model.GameDataItem
import com.lucasbueno.thenamegamewt.features.game.presentation.view.adapter.PersonCardAdapter
import com.lucasbueno.thenamegamewt.features.onboarding.presentation.viewmodel.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private val onboardingViewModel: OnboardingViewModel by viewModels()

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOnboardingBinding.bind(view)
        setClickListeners()
        initObservers()
    }

    private fun setClickListeners() {
        binding.practiceModeButton.setOnClickListener {
            onboardingViewModel.onPracticeModeClicked()
        }

        binding.timedModeButton.setOnClickListener {
            onboardingViewModel.onTimedModeClicked()
        }
    }

    private fun initObservers() {
        onboardingViewModel.eventFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleEvent(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleEvent(event: UiEvent) {
        when (event) {
            is UiEvent.NavigateToGameScreen -> {
                val action =
                    OnboardingFragmentDirections.actionOnboardingFragmentToGameScreenFragment(
                        isPractice = event.isPracticeMode
                    )
                findNavController().navigateUp()
                findNavController().navigate(action)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}