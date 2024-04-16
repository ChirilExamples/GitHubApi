package com.example.githubapi.data.repository

import com.example.githubapi.data.data_structure.GitHubStructureItem
import com.example.githubapi.domain.utils.Resource
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getAllData(): Flow<Resource<List<GitHubStructureItem>>>
}