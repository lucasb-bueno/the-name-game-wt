package com.lucasbueno.thenamegamewt.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lucasbueno.thenamegamewt.features.game.data.datasource.GameScreenRemoteDataSourceImpl
import com.lucasbueno.thenamegamewt.features.game.data.repository.GameScreenRepositoryImpl
import com.lucasbueno.thenamegamewt.features.game.data.service.GameService
import com.lucasbueno.thenamegamewt.features.game.domain.repository.GameScreenRepository
import com.lucasbueno.thenamegamewt.utils.getUnsafeOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson): Retrofit = Retrofit.Builder()
        .baseUrl("https://namegame.willowtreeapps.com/api/")
        .client(getUnsafeOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun provideGameService(retrofit: Retrofit): GameService =
        retrofit.create(GameService::class.java)

    @Provides
    @Singleton
    fun providesGameScreenRemoteDataSource(service: GameService): GameScreenRemoteDataSourceImpl {
        return GameScreenRemoteDataSourceImpl(service)
    }

    @Provides
    @Singleton
    fun provideGameScreenRepository(
        remoteDataSource: GameScreenRemoteDataSourceImpl
    ): GameScreenRepository = GameScreenRepositoryImpl(remoteDataSource)
}

