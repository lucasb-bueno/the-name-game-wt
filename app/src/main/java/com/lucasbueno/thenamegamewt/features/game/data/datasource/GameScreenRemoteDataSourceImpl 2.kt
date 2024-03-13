package com.lucasbueno.thenamegamewt.features.game.data.datasource

import com.lucasbueno.thenamegamewt.features.game.data.model.GameDataResponseItem
import com.lucasbueno.thenamegamewt.features.game.data.service.GameService
import javax.inject.Inject

class GameScreenRemoteDataSourceImpl @Inject constructor(private val service: GameService) {
    suspend fun getData(): List<GameDataResponseItem> {
        return service.getData()
    }
}