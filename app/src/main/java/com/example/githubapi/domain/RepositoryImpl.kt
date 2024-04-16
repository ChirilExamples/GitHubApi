package com.example.githubapi.domain

import android.util.Log
import com.example.githubapi.data.data_structure.GitHubStructureItem
import com.example.githubapi.data.remote.HubService
import com.example.githubapi.data.repository.Repository
import com.example.githubapi.domain.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDatasource: HubService
) : Repository {
    override suspend fun getAllData(): Flow<Resource<List<GitHubStructureItem>>> = flow {
        emit(Resource.Loading())
        try {
            Log.i("DebugNetworkRepo", "Pass getAllNews ${remoteDatasource.getAllData()}")
            val response = remoteDatasource.getAllData()

            if (response.isSuccessful) response.body()?.let {
                emit(Resource.Success(it))
                Log.i("DebugNetworkRepoLet", response.toString())
            }
            else emit(Resource.Error(response.code().toString()))
        } catch (e: HttpException) {
            emit(Resource.Error("Could not load data"))
            Log.i("DebugNetworkRepo", "load data error")
        } catch (e: IOException) {
            emit(Resource.Error("Check internet or server"))
            Log.i("DebugNetworkRepo", "check internet error")
        }
    }
}
