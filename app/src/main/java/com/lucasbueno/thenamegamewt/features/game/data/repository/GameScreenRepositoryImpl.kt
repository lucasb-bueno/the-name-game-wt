package com.lucasbueno.thenamegamewt.features.game.data.repository

import com.lucasbueno.thenamegamewt.features.game.data.datasource.GameScreenRemoteDataSourceImpl
import com.lucasbueno.thenamegamewt.features.game.data.model.toGameDataItem
import com.lucasbueno.thenamegamewt.features.game.data.service.GameService
import com.lucasbueno.thenamegamewt.features.game.domain.model.GameDataItem
import com.lucasbueno.thenamegamewt.features.game.domain.repository.GameScreenRepository
import javax.inject.Inject

class GameScreenRepositoryImpl @Inject constructor(
    private val remoteDataSource: GameScreenRemoteDataSourceImpl
) :
    GameScreenRepository {
    override suspend fun getData(): List<GameDataItem> {
        return remoteDataSource.getData().map { it.toGameDataItem() }
    }
}