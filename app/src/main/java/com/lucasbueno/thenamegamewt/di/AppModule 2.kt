package com.lucasbueno.thenamegamewt.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lucasbueno.thenamegamewt.features.game.data.datasource.GameScreenRemoteDataSourceImpl
import com.lucasbueno.thenamegamewt.features.game.data.repository.GameScreenRepositoryImpl
import com.lucasbueno.thenamegamewt.features.game.data.service.GameService
import com.lucasbueno.thenamegamewt.features.game.domain.repository.GameScreenRepository
import com.lucasbueno.thenamegamewt.features.game.domain.usecase.GetGameDataUseCase
import com.lucasbueno.thenamegamewt.utils.getUnsafeOkHttpClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import javax.inject.Singleton
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

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

@Module
@InstallIn(ActivityRetainedComponent::class)
object DomainModule {
    @Provides
    fun provideUseCase(repositoryImpl: GameScreenRepository): GetGameDataUseCase {
        return GetGameDataUseCase(repositoryImpl)
    }
}

