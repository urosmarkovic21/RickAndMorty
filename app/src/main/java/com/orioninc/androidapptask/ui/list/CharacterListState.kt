package com.orioninc.androidapptask.ui.list
import com.orioninc.androidapptask.data.model.Character

sealed class CharacterListState {
    object Loading : CharacterListState()
    data class Success(val data: List<Character>): CharacterListState()
    data class Error(val message: String): CharacterListState()
}