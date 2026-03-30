package com.orioninc.androidapptask.ui.list

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
class CharacterListViewModelTest {
    private lateinit var fakeRepository: FakeCharacterRepository
    private lateinit var viewModel: CharacterListViewModel

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
    fun `loadFirstPage success updates state`() =
        runTest {
            fakeRepository.charactersPage1 =
                listOf(
                    createCharacter(id = 1, name = "Rick"),
                )

            viewModel = CharacterListViewModel(fakeRepository)
            advanceUntilIdle()
            val state = viewModel.state.value

            assert(state.characters == fakeRepository.charactersPage1)
            assert(!state.isInitialLoading)
            assert(state.errorMessage == null)
        }

    @Test
    fun `loadFirstPage error updates state with error`() =
        runTest {
            fakeRepository.shouldThrowCharactersError = true

            viewModel = CharacterListViewModel(fakeRepository)
            advanceUntilIdle()
            val state = viewModel.state.value

            assert(state.characters.isEmpty())
            assert(!state.isInitialLoading)
            assert(state.errorMessage != null)
        }

    @Test
    fun `pagination adds new characters`() =
        runTest {
            fakeRepository.charactersPage1 =
                listOf(
                    createCharacter(1, "Rick"),
                )

            fakeRepository.charactersPage2 =
                listOf(
                    createCharacter(2, "Morty"),
                )

            viewModel = CharacterListViewModel(fakeRepository)

            advanceUntilIdle()

            viewModel.loadNextPage()

            advanceUntilIdle()

            val state = viewModel.state.value

            assert(state.characters.size == 2)
            assert(
                state.characters == fakeRepository.charactersPage1 + fakeRepository.charactersPage2,
            )
            assert(state.errorMessage == null)
        }

    @Test
    fun `pagination error shows error message`() =
        runTest {
            fakeRepository.charactersPage1 =
                listOf(
                    createCharacter(1, "Rick"),
                )

            viewModel = CharacterListViewModel(fakeRepository)

            advanceUntilIdle()

            fakeRepository.shouldThrowCharactersError = true

            viewModel.loadNextPage()

            advanceUntilIdle()

            val state = viewModel.state.value

            assert(state.errorMessage != null)
        }

    @Test
    fun `loadFirstPage sets loading state correctly`() =
        runTest {
            fakeRepository.charactersPage1 =
                listOf(
                    createCharacter(1, "Rick"),
                )

            viewModel = CharacterListViewModel(fakeRepository)

            val stateDuringLoad = viewModel.state.value
            assert(stateDuringLoad.isInitialLoading)

            advanceUntilIdle()

            val stateAfterLoad = viewModel.state.value
            assert(!stateAfterLoad.isInitialLoading)
        }

    @Test
    fun `requestInProgress prevents multiple requests`() =
        runTest {
            fakeRepository.charactersPage1 =
                listOf(
                    createCharacter(1, "Rick"),
                )

            viewModel = CharacterListViewModel(fakeRepository)

            advanceUntilIdle()

            viewModel.loadNextPage()
            viewModel.loadNextPage()

            advanceUntilIdle()

            val state = viewModel.state.value

            assert(state.characters.size == 1)
        }

    @Test
    fun `loadNextPage does nothing when last page reached`() =
        runTest {
            fakeRepository.charactersPage1 =
                listOf(
                    createCharacter(1, "Rick"),
                )

            viewModel = CharacterListViewModel(fakeRepository)

            advanceUntilIdle()

            fakeRepository.charactersPage2 = emptyList()

            viewModel.loadNextPage()

            advanceUntilIdle()

            val state = viewModel.state.value

            assert(state.characters.size == 1)
        }

    @Test
    fun `last page is reached correctly`() =
        runTest {
            fakeRepository.charactersPage1 =
                listOf(
                    createCharacter(1, "Rick"),
                )

            fakeRepository.charactersPage2 =
                listOf(
                    createCharacter(2, "Morty"),
                )

            viewModel = CharacterListViewModel(fakeRepository)

            advanceUntilIdle()

            viewModel.loadNextPage()

            advanceUntilIdle()

            val state = viewModel.state.value

            assert(state.endReached)
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
