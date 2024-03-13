package com.lucasbueno.thenamegamewt.features.game.data.service

import com.lucasbueno.thenamegamewt.features.game.data.model.GameDataResponseItem
import retrofit2.http.GET

interface GameService {
    @GET("v1.0/profiles")
    suspend fun getData(): List<GameDataResponseItem>
}