package com.example.githubapi.data.remote

import javax.inject.Inject

class HubRemoteDatasource @Inject constructor(private val hubService: HubService) {
    suspend fun getAllData() = hubService.getAllData()
}
