package com.lucasbueno.thenamegamewt.features.onboarding.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(

): ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onPracticeModeClicked() {
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.NavigateToGameScreen(isPracticeMode = true))
        }
    }

    fun onTimedModeClicked() {
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.NavigateToGameScreen(isPracticeMode = false))
        }
    }
}

sealed class UiEvent {
    data class NavigateToGameScreen(val isPracticeMode: Boolean): UiEvent()
}