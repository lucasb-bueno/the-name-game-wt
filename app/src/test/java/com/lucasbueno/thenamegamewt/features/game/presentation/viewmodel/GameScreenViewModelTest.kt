package com.lucasbueno.thenamegamewt.features.game.presentation.viewmodel

import app.cash.turbine.test
import com.lucasbueno.thenamegamewt.features.game.domain.usecase.GetGameDataUseCase
import com.lucasbueno.thenamegamewt.stub.GameDataStub
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GameScreenViewModelTest {

    private lateinit var viewModel: GameScreenViewModel
    private val getGameDataUseCase: GetGameDataUseCase = mockk()

    @Before
    fun setup() {
        viewModel = GameScreenViewModel(getGameDataUseCase)
    }

    @Test
    fun `getData when is success should update state`() = runBlocking {
        // Given
        val expected = GameDataState(
            gameDataList = GameDataStub.gameDataListStub
        )
        coEvery { getGameDataUseCase() } returns GameDataStub.gameDataListStub

        // When
        viewModel.getData()

        viewModel.stateFlow.test {
            assertEquals(awaitItem(), expected)
        }
    }
}