package com.lucasbueno.thenamegamewt.features.game.presentation.viewmodel

import com.lucasbueno.thenamegamewt.features.game.domain.usecase.GetGameDataUseCase
import io.mockk.mockk
import org.junit.After
import org.junit.Test

import org.junit.Before

class GameScreenViewModelTest {

    private lateinit var viewModel: GameScreenViewModel
    private val getGameDataUseCase: GetGameDataUseCase = mockk()

    @Before
    fun setUp() {
        viewModel = GameScreenViewModel(getGameDataUseCase)
    }

    @Test
    fun `get game data success emits correct state`() {
        // Given

        // When

        // Then
    }

    @After
    fun tearDown() {
    }
}