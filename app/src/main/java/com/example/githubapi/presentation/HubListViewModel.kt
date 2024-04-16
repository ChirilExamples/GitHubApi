package com.example.githubapi.presentation

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubapi.data.data_structure.GitHubStructureItem
import com.example.githubapi.data.repository.Repository
import com.example.githubapi.domain.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HubListViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _itemsList: MutableStateFlow<Resource<List<GitHubStructureItem>>> =
        MutableStateFlow(Resource.Loading())
    val itemsList: StateFlow<Resource<List<GitHubStructureItem>>> = _itemsList

    val scrollState = LazyListState()

    init {
        getData()
    }

    fun getData() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllData().collectLatest { _itemsList.emit(it) }
//        Log.i("DebugNetworkVM", itemsList.value.data.toString())
    }
}
