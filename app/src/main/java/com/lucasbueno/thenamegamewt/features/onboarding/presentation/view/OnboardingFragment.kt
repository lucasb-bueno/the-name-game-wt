package com.lucasbueno.thenamegamewt.features.onboarding.presentation.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.lucasbueno.thenamegamewt.R
import com.lucasbueno.thenamegamewt.databinding.FragmentOnboardingBinding
import com.lucasbueno.thenamegamewt.features.onboarding.presentation.viewmodel.OnboardingViewModel
import com.lucasbueno.thenamegamewt.features.onboarding.presentation.viewmodel.UiEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private val viewModel: OnboardingViewModel by viewModels()

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOnboardingBinding.bind(view)
        initObservers()
        onClickListeners()
    }

    private fun initObservers() {
        viewModel.eventFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleEvent(it) }
            .launchIn(lifecycleScope)
    }

    private fun onClickListeners() {
        binding.practiceModeButton.setOnClickListener {
            viewModel.onPracticeModeClicked()
        }
        binding.timedModeButton.setOnClickListener {
            viewModel.onTimedModeCLicked()
        }
    }

    private fun handleEvent(event: UiEvent) {
        when(event) {
            is UiEvent.NavigateToGameScreen -> {
                val action =
                    OnboardingFragmentDirections.actionOnboardingFragmentToGameScreenFragment(
                        isPractice = event.isPractice
                    )
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}