package com.lucasbueno.thenamegamewt.features.game.domain.repository

import com.lucasbueno.thenamegamewt.features.game.domain.model.GameDataItem

interface GameScreenRepository {
    suspend fun getData(): List<GameDataItem>
}