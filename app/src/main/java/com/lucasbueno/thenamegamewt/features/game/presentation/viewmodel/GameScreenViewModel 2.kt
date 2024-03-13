package com.lucasbueno.thenamegamewt.features.game.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucasbueno.thenamegamewt.features.game.domain.model.GameDataItem
import com.lucasbueno.thenamegamewt.features.game.domain.usecase.GetGameDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val NUMBER_OF_CARDS = 6

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val useCase: GetGameDataUseCase
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<GameScreenState>(GameScreenState.Empty)
    val stateFlow = _stateFlow.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

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
            if (gameDataItem.id == correctAnswerId) {
                _eventFlow.emit(UiEvent.ShowSnackBar)
            } else {
                _eventFlow.emit(UiEvent.ShowGameOVerDialog)
            }
        }
    }

    private fun handleSuccess(list: List<GameDataItem>) {
        val randomList = list.shuffled().take(NUMBER_OF_CARDS)
        val correctAnswerId = randomList.shuffled().take(1)
        _stateFlow.value = GameScreenState.Success(
            data = randomList,
            correctAnswerId = correctAnswerId.first()
        )
    }

}

sealed class GameScreenState {
    object Empty : GameScreenState()
    data class Success(
        val data: List<GameDataItem> = listOf(),
        val correctAnswerId: GameDataItem,
        val toolBarTitle: String = "Practice Mode",
        val isTimerActive: Boolean = false
    ) : GameScreenState()
}

sealed class UiEvent {
    data class Error(val error: Throwable) : UiEvent()
    object ShowGameOVerDialog : UiEvent()
    object ShowSnackBar : UiEvent()
}