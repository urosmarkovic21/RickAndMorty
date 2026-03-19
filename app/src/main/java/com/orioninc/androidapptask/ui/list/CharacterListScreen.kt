package com.orioninc.androidapptask.ui.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.orioninc.androidapptask.R

@Composable
fun CharacterListScreen(
    viewModel: CharacterListViewModel,
    onCharacterClick: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (state) {
            is CharacterListState.Loading -> {
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is CharacterListState.Error -> {
                val message = (state as CharacterListState.Error).message
                Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = message)
                        Button(onClick = { viewModel.retry() }) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }
            }

            is CharacterListState.Success -> {
                val characters = (state as CharacterListState.Success).data
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = innerPadding
                ) {
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
}