package com.uas.pmb.di

import android.content.Context
import com.uas.pmb.data.PmbApiService
import com.uas.pmb.data.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): PmbApiService = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8080/") // IP Localhost dari Emulator
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(PmbApiService::class.java)

    @Provides
    @Singleton
    fun provideTokenManager(@ApplicationContext context: Context) = TokenManager(context)
}