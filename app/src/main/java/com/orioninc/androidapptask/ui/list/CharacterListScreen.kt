package com.orioninc.androidapptask.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orioninc.androidapptask.data.repository.CharacterRepository
import com.orioninc.androidapptask.data.network.RetrofitInstance

@Composable
fun CharacterListScreen(
    onCharacterClick: (Int) -> Unit
) {
    val repository = CharacterRepository(RetrofitInstance.api)
    val factory = CharacterListViewModelFactory(repository)
    val viewModel: CharacterListViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsState()

    when (state) {
        is CharacterListState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is CharacterListState.Error -> {
            val message = (state as CharacterListState.Error).message
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = message)
                    Button(onClick = { viewModel.retry() }) {
                        Text("Retry")
                    }
                }
            }
        }
        is CharacterListState.Success -> {
            val characters = (state as CharacterListState.Success).data
            LazyColumn (modifier = Modifier.systemBarsPadding()){
                items(characters) { character ->
                    CharacterItem(
                        character = character,
                        onClick = { onCharacterClick(character.id) }
                    )
                }
            }
        }
    }
}