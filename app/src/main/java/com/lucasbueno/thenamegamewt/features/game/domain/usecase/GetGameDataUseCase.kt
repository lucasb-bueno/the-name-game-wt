package com.lucasbueno.thenamegamewt.features.game.domain.usecase

import com.lucasbueno.thenamegamewt.features.game.domain.model.GameDataItem
import com.lucasbueno.thenamegamewt.features.game.domain.repository.GameScreenRepository
import javax.inject.Inject

class GetGameDataUseCase @Inject constructor(
    private val repository: GameScreenRepository
) {
    suspend operator fun invoke(): List<GameDataItem> {
        return repository.getData()
    }
}