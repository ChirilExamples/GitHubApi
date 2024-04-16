package com.example.githubapi.data.injections

import com.example.githubapi.data.remote.HubRemoteDatasource
import com.example.githubapi.data.remote.HubService
import com.example.githubapi.data.repository.Repository
import com.example.githubapi.domain.RepositoryImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Injection {

    @Provides
    fun provideGSON(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideAPIService(gson: Gson): Retrofit =
        Retrofit.Builder().baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

    @Provides
    fun provideHubService(retrofit: Retrofit): HubService = retrofit.create(HubService::class.java)

    @Singleton
    @Provides
    fun provideHubRemoteDatasource(hubService: HubService) = HubRemoteDatasource(hubService)

    @Singleton
    @Provides
    fun provideRepository(remoteDatasource: HubService): Repository =
        RepositoryImpl(remoteDatasource)
}
