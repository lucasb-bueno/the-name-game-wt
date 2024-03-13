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

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val getGameDataUseCase: GetGameDataUseCase
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(GameDataState())
    val stateFlow = _stateFlow.asStateFlow()

    private val _eventFlow = MutableSharedFlow<GameUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getData() = viewModelScope.launch {
        runCatching {
            getGameDataUseCase()
        }.onSuccess {
            _stateFlow.value = stateFlow.value.copy(gameDataList = it)
        }.onFailure {
            _eventFlow.emit(GameUiEvent.Error(it))
        }
    }

}

data class GameDataState(
    val gameDataList: List<GameDataItem> = listOf()
)

sealed class GameUiEvent {
    data class Error(val error: Throwable) : GameUiEvent()
}