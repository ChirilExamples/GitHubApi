package com.example.githubapi.presentation.main_screen

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.githubapi.data.data_structure.GitHubStructureItem
import com.example.githubapi.domain.utils.Resource
import com.example.githubapi.presentation.AppsTopAppBar
import com.example.githubapi.presentation.HubListViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/*
Moved to a separate package for maintainability if/when the app grows,
As well as if moving some composable functions would be moved to a separate class
*/
class MainScreenContent {
    @Composable
    fun MainScreen(viewModel: HubListViewModel = viewModel(LocalContext.current as ComponentActivity)) {
        val state by viewModel.itemsList.collectAsState()
        val listState = viewModel.scrollState
        val scope = rememberCoroutineScope()
        Scaffold(modifier = Modifier, topBar = { AppsTopAppBar(text = "Git Repos") }, content = {
            when (state) {
                is Resource.Success -> {
                    ScaffoldContentMainScreen(
                        state = state as Resource.Success<List<GitHubStructureItem>>,
                        viewModel = viewModel,
                        padding = it
                    )
                    val showButton by remember {
                        derivedStateOf {
                            listState.firstVisibleItemIndex > 0 && !listState.isScrollInProgress
                        }
                    }
                    AnimatedVisibility(
                        visible = showButton,
                        enter = fadeIn(),
                        exit = fadeOut(),
                    ) {
                        ScrollToTopButton(onClick = {
                            scope.launch {
                                listState.animateScrollToItem(0)
                            }
                        })
                    }
                }

                is Resource.Error -> {
                    state.message?.let { message ->
                        ShowErrorMessage(message = message)
                    }
                    // Reloading the page every 5 seconds for if the connection failed (beside the reload button)
                    LaunchedEffect(key1 = "") {
                        delay(5000)
                        viewModel.getData()
                        Log.i("MainScreen", "reloading after error")
                    }
                }

                is Resource.Loading -> {
                    ShowLoadingIndicator()
                    Log.i("DebugNetworkMainLoading", "Loading")
                }
            }
        })
    }

    @Composable
    fun ScaffoldContentMainScreen(
        padding: PaddingValues,
        viewModel: HubListViewModel,
        state: Resource.Success<List<GitHubStructureItem>>
    ) {
        val list = state.data!!.asReversed()
        Log.i("DebugNetworkLazyState", list.toString())

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .padding(padding), state = viewModel.scrollState
        ) {
            items(list) {
                AllItems(it)
            }

        }
    }

    @Composable
    fun ShowLoadingIndicator() {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }

    @Composable
    fun ShowErrorMessage(
        viewModel: HubListViewModel = viewModel(LocalContext.current as ComponentActivity),
        message: String
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column {
                Text(
                    text = "$message, it will reload soon",
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { viewModel.getData() },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(0.3f),
                    ) {
                        Text(text = "Reload", color = Color.White)
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AllItems(it: GitHubStructureItem) {
        val uriHandler = LocalUriHandler.current
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
            onClick = {
                uriHandler.openUri(it.html_url)
            }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                GlideImage(
                    imageModel = "https://github.githubassets.com/assets/GitHub-Mark-ea2971cee799.png",
                    modifier = Modifier
                        .width(130.dp)
                        .padding(10.dp),
                    contentScale = ContentScale.Fit
                )
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = it.name.replaceFirstChar(Char::titlecase),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            top = 10.dp, end = 10.dp
                        ),
                        maxLines = 1
                    )
                    Text(
                        //for scenario if the API response provides different
                        text = "Made by: ${it.owner.login.replaceFirstChar(Char::titlecase)}",
                        fontSize = 12.sp
                    )
                    Text(
                        //for scenario if the API response provides different
                        text = "Id: ${it.id}", fontSize = 12.sp
                    )
                    // When the API has an empty field, it would crash the app
                    if (it.description != null) {
                        Text(
                            text = it.description,
                            maxLines = 3,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier,
                            fontWeight = FontWeight.Light
                        )
                    }

                }
            }
        }
    }

    @Composable
    fun ScrollToTopButton(onClick: () -> Unit) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(bottom = 20.dp), Alignment.BottomCenter
        ) {
            Button(
                onClick = { onClick() },
                modifier = Modifier
                    .shadow(10.dp, shape = CircleShape)
                    .size(65.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                ),
            ) {
                Icon(
                    Icons.Filled.KeyboardArrowUp,
                    "arrow up",
                )
            }
        }
    }
}