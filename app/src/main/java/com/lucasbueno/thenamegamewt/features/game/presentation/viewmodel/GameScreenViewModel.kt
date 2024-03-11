package com.lucasbueno.thenamegamewt.features.game.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasbueno.thenamegamewt.features.game.domain.model.GameDataItem
import com.lucasbueno.thenamegamewt.features.game.domain.usecase.GetGameDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NUMBER_OF_CARDS = 6

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val useCase: GetGameDataUseCase
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<GameScreenState>(GameScreenState())
    val stateFlow = _stateFlow.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val timerDuration = 6000L
    private var currentTime = timerDuration
    private var timerJob: Job? = null

    fun getGameData() {
        viewModelScope.launch {
            runCatching {
                useCase.invoke()
            }.onSuccess { data ->
                handleSuccess(data)
            }.onFailure {
                _eventFlow.emit(UiEvent.Error(it))
            }
        }
    }

    fun onItemClicked(gameDataItem: GameDataItem, correctAnswerId: String?) {
        viewModelScope.launch {
            val isCorrect = gameDataItem.id == correctAnswerId
            _stateFlow.value = stateFlow.value.copy(isAnswerCorrectState = isCorrect)

            if (isCorrect) {
                _eventFlow.emit(UiEvent.ShowSuccessAction)
                addToCounter()
            } else if (stateFlow.value.isPracticeMode) {
                _eventFlow.emit(UiEvent.ShowGameOverDialog)
            }
        }
    }

    private fun handleSuccess(list: List<GameDataItem>) {
        val randomList = list.shuffled().take(NUMBER_OF_CARDS)
        val correctAnswerId = randomList.shuffled().take(1)

        _stateFlow.value = stateFlow.value.copy(
            data = randomList,
            correctAnswerId = correctAnswerId.first()
        )
    }

    fun startTimer() {
        if (timerJob?.isActive == true) {
            return
        }

        timerJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis()
            val endTime = startTime + timerDuration

            while (currentTime > 0 && isActive) {
                currentTime = endTime - System.currentTimeMillis()
                if (currentTime < 0) {
                    currentTime = 0
                }

                _stateFlow.value = stateFlow.value.copy(
                    progressState = (currentTime.toFloat() / timerDuration * 100).toInt()
                )

                delay(100)
            }

            if (currentTime <= 0) {
                _eventFlow.emit(UiEvent.ShowGameOverDialog)
            }
        }
    }

    private fun addToCounter() {
        _stateFlow.value = stateFlow.value.copy(counter = stateFlow.value.counter + 1)
    }

    fun onVerifyTimer() {
        if (stateFlow.value.progressState <= 0) {
                viewModelScope.launch {
                _eventFlow.emit(UiEvent.ShowGameOverDialog)
            }
        }
    }

    fun setGameMode(isPracticeMode: Boolean) {
        _stateFlow.value = stateFlow.value.copy(isPracticeMode = isPracticeMode)
    }
}

data class GameScreenState(
    val data: List<GameDataItem> = listOf(),
    val correctAnswerId: GameDataItem? = null,
    val progressState: Int = 100,
    val counter: Int = 0,
    val isAnswerCorrectState: Boolean = false,
    val isPracticeMode: Boolean = true
)

sealed class UiEvent {
    data class Error(val error: Throwable) : UiEvent()
    object ShowGameOverDialog : UiEvent()
    object ShowSuccessAction : UiEvent()
}