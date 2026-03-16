package com.orioninc.androidapptask.ui.detail
import com.orioninc.androidapptask.data.model.Character

sealed class CharacterDetailState {
    object Loading : CharacterDetailState()
    data class Success(val character: Character) : CharacterDetailState()
    data class Error(val message: String) : CharacterDetailState()
}