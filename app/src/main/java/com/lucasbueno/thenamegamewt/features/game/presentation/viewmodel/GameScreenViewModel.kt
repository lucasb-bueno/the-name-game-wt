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
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NUMBER_OF_CARDS = 6
private const val TIMER_DELAY = 50L
private const val ZERO_INT = 0
private const val TIMER_DURATION = 6000L

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val useCase: GetGameDataUseCase
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(GameScreenState())
    val stateFlow = _stateFlow.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var timerJob: Job? = null

    fun setGameMode(isPracticeMode: Boolean) = viewModelScope.launch {
        _stateFlow.value = stateFlow.value.copy(isPracticeMode = isPracticeMode)
        if (!isPracticeMode) {
            startTimer()
        }
    }

    fun getGameData() {
        viewModelScope.launch {
            runCatching {
                useCase.invoke()
            }.onSuccess { data ->
                handleSuccess(data)
            }.onFailure { error ->
                _eventFlow.emit(UiEvent.Error(error))
            }
        }
    }

    fun onItemClicked(gameDataItem: GameDataItem, correctAnswerId: String?) {
        viewModelScope.launch {
            val isCorrect = gameDataItem.id == correctAnswerId
            _stateFlow.value = stateFlow.value.copy(isAnswerCorrectState = isCorrect)

            if (isCorrect) {
                _eventFlow.emit(UiEvent.ShowSuccessAction)
                addToCounterAndVerifyPoints()
            } else if (stateFlow.value.isPracticeMode) {
                _eventFlow.emit(UiEvent.ShowGameOverDialog)
            }
        }
    }

    private fun handleSuccess(list: List<GameDataItem>) {
        val randomList = list.shuffled().take(NUMBER_OF_CARDS)
        val correctAnswerItem = randomList.shuffled().first()

        _stateFlow.value = stateFlow.value.copy(
            data = randomList,
            correctAnswerItem = correctAnswerItem
        )
    }

    private fun startTimer() {
        if (timerJob?.isActive == true) {
            return
        }

        timerJob = viewModelScope.launch {
            setTimerVisible(true)
            val startTime = System.currentTimeMillis()
            var remainingTime = TIMER_DURATION

            while (remainingTime > ZERO_INT) {
                remainingTime = TIMER_DURATION - (System.currentTimeMillis() - startTime)

                if (remainingTime <= ZERO_INT) {
                    _eventFlow.emit(UiEvent.ShowGameOverDialog)
                }

                _stateFlow.value = stateFlow.value.copy(
                    progressState = (remainingTime.toFloat() / TIMER_DURATION * 100).toInt()
                )

                delay(TIMER_DELAY)
            }

            _eventFlow.emit(UiEvent.ShowGameOverDialog)
            setTimerVisible(false)
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    private fun setTimerVisible(isVisible: Boolean) {
        _stateFlow.value = _stateFlow.value.copy(isTimerVisible = isVisible)
    }

    private fun addToCounterAndVerifyPoints() = viewModelScope.launch {
        val listSize = stateFlow.value.data.size
        if (stateFlow.value.counter >= listSize) {
            _eventFlow.emit(UiEvent.ShowGameOverDialog)
            stopTimer()
        }
        _stateFlow.value = stateFlow.value.copy(counter = stateFlow.value.counter + 1)
    }
}

data class GameScreenState(
    val data: List<GameDataItem> = listOf(),
    val correctAnswerItem: GameDataItem? = null,
    val progressState: Int = 100,
    val counter: Int = 0,
    val isAnswerCorrectState: Boolean = false,
    val isPracticeMode: Boolean = true,
    val isTimerVisible: Boolean = false
)

sealed class UiEvent {
    data class Error(val error: Throwable) : UiEvent()
    data object ShowGameOverDialog : UiEvent()
    data object ShowSuccessAction : UiEvent()
}