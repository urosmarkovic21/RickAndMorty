package com.orioninc.androidapptask.ui.detail

import com.orioninc.androidapptask.data.model.Character
import com.orioninc.androidapptask.domain.GetCharacterUseCase
import com.orioninc.androidapptask.domain.RefreshCharacterUseCase
import com.orioninc.androidapptask.fake.FakeCharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var fakeRepository: FakeCharacterRepository
    private lateinit var viewModel: CharacterDetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeCharacterRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(characterId: Int = 1) {
        viewModel = CharacterDetailViewModel(
            getCharacterUseCase = GetCharacterUseCase(fakeRepository),
            refreshCharacterUseCase = RefreshCharacterUseCase(fakeRepository),
            characterId = characterId,
        )
    }

    @Test
    fun `fetchCharacter success updates state to Success`() = runTest {
        val character = createCharacter(1, "Rick")
        fakeRepository.character = character

        createViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value

        assertTrue(state is CharacterDetailState.Success)
        assertEquals(character, (state as CharacterDetailState.Success).character)
    }

    @Test
    fun `fetchCharacter error updates state to Error`() = runTest {
        fakeRepository.shouldThrowCharacterError = true

        createViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value

        assertTrue(state is CharacterDetailState.Error)
    }

    @Test
    fun `initial state is Loading`() = runTest {
        val character = createCharacter(1, "Rick")
        fakeRepository.character = character

        createViewModel()

        val state = viewModel.state.value

        assertTrue(state is CharacterDetailState.Loading)
    }

    private fun createCharacter(
        id: Int,
        name: String = "Test",
        gender: String = "Unknown",
        image: String = "",
        species: String = "Unknown",
        status: String = "Alive",
    ) = Character(
        id = id,
        name = name,
        gender = gender,
        image = image,
        species = species,
        status = status,
    )
}