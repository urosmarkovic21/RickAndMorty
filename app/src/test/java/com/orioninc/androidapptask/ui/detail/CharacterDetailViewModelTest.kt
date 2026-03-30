package com.orioninc.androidapptask.ui.detail

import com.orioninc.androidapptask.data.model.Character
import com.orioninc.androidapptask.fake.FakeCharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterDetailViewModelTest {
    private lateinit var fakeRepository: FakeCharacterRepository
    private lateinit var viewModel: CharacterDetailViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        fakeRepository = FakeCharacterRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchCharacter success updates state to Success`() =
        runTest {
            val character = createCharacter(1, "Rick")
            fakeRepository.character = character

            viewModel = CharacterDetailViewModel(fakeRepository, 1)

            advanceUntilIdle()

            val state = viewModel.state.value

            assert(state is CharacterDetailState.Success)
            assert((state as CharacterDetailState.Success).character == character)
        }

    @Test
    fun `fetchCharacter error updates state to Error`() =
        runTest {
            fakeRepository.shouldThrowCharacterError = true

            viewModel = CharacterDetailViewModel(fakeRepository, 1)

            advanceUntilIdle()

            val state = viewModel.state.value

            assert(state is CharacterDetailState.Error)
        }

    @Test
    fun `initial state is Loading`() =
        runTest {
            val character = createCharacter(1, "Rick")
            fakeRepository.character = character

            viewModel = CharacterDetailViewModel(fakeRepository, 1)

            val state = viewModel.state.value

            assert(state is CharacterDetailState.Loading)
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
