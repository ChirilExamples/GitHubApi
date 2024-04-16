package com.example.githubapi.data.remote

import com.example.githubapi.data.data_structure.GitHubStructureItem
import retrofit2.Response
import retrofit2.http.GET

interface HubService {
    @GET("orgs/square/repos")
    suspend fun getAllData(): Response<List<GitHubStructureItem>>
}
