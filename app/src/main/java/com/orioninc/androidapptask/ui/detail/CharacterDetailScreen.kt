package com.orioninc.androidapptask.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.orioninc.androidapptask.data.network.RetrofitInstance
import com.orioninc.androidapptask.data.repository.CharacterRepository

@Composable
fun CharacterDetailScreen(
    characterId: Int,
    onBackClick: () -> Unit
) {
    val repository = CharacterRepository(RetrofitInstance.api)
    val factory = CharacterDetailViewModelFactory(repository, characterId)
    val viewModel: CharacterDetailViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        when (state) {
            is CharacterDetailState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is CharacterDetailState.Error -> {
                val message = (state as CharacterDetailState.Error).message
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = message)
                }
            }
            is CharacterDetailState.Success -> {
                val character = (state as CharacterDetailState.Success).character
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = character.image,
                        contentDescription = character.name,
                        modifier = Modifier.size(200.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = character.name, style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Status: ${character.status}")
                    Text(text = "Species: ${character.species}")
                    Text(text = "Gender: ${character.gender}")
                }
            }
        }
    }
}