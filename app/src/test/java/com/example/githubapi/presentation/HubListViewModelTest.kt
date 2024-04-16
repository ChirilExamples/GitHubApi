package com.example.githubapi.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.githubapi.data.data_structure.GitHubStructureItem
import com.example.githubapi.data.data_structure.Owner
import com.example.githubapi.data.repository.Repository
import com.example.githubapi.domain.utils.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class HubListViewModelTest {

    @get:Rule
    val instantTaskExecutionRule = InstantTaskExecutorRule()

    private val hubList = listOf(
        GitHubStructureItem("Desc", "link", 1, "", Owner("Square")),
        GitHubStructureItem("desc2", "link2", 2, "", Owner("Circle"))
    )
    private val flow = flow<Resource<List<GitHubStructureItem>>> {
        emit(Resource.Success(hubList))
    }

    private val repository: Repository = mockk {
        coEvery { getAllData() } returns flow
    }

    private val viewModel = HubListViewModel(repository)

    @Test
    fun `get data should return emit list of things`() = runBlocking {
        viewModel.getData()
        delay(1000)
        assertTrue(viewModel.itemsList.value is Resource.Success)
        assertEquals(hubList, (viewModel.itemsList.value as Resource.Success).data)
    }

    @Test
    fun `get data should be in loading at the click instant`() = runBlocking {
        viewModel.getData()
        assertTrue(viewModel.itemsList.value is Resource.Loading)
    }
}