package com.orioninc.androidapptask.ui.list

import com.orioninc.androidapptask.data.model.Character
import com.orioninc.androidapptask.data.model.CharactersRefreshResult
import com.orioninc.androidapptask.domain.GetCharactersUseCase
import com.orioninc.androidapptask.domain.GetSavedCharactersRefreshResultUseCase
import com.orioninc.androidapptask.domain.RefreshCharactersUseCase
import com.orioninc.androidapptask.fake.FakeCharacterRepository
import com.orioninc.androidapptask.util.NotificationHelper
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var fakeRepository: FakeCharacterRepository
    private lateinit var viewModel: CharacterListViewModel
    private lateinit var notificationHelper: NotificationHelper

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeCharacterRepository()
        notificationHelper = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() {
        viewModel =
            CharacterListViewModel(
                getCharactersUseCase = GetCharactersUseCase(fakeRepository),
                refreshCharactersUseCase = RefreshCharactersUseCase(fakeRepository),
                getSavedCharactersRefreshResultUseCase =
                    GetSavedCharactersRefreshResultUseCase(fakeRepository),
                notificationHelper = notificationHelper,
            )
    }

    @Test
    fun `loadFirstPage success updates state`() =
        runTest {
            fakeRepository.characters =
                listOf(
                    createCharacter(id = 1, name = "Rick"),
                )
            fakeRepository.savedRefreshResult =
                CharactersRefreshResult(
                    nextPage = 2,
                    isLastPage = false,
                )

            createViewModel()
            advanceUntilIdle()

            val state = viewModel.state.value

            assertTrue(state.characters == fakeRepository.characters)
            assertFalse(state.isInitialLoading)
            assertNull(state.errorMessage)
        }

    @Test
    fun `loadFirstPage error updates state with error`() =
        runTest {
            fakeRepository.shouldThrowCharactersError = true

            createViewModel()
            advanceUntilIdle()

            val state = viewModel.state.value

            assertTrue(state.characters.isEmpty())
            assertFalse(state.isInitialLoading)
            assertNotNull(state.errorMessage)
        }

    @Test
    fun `pagination adds new characters`() =
        runTest {
            fakeRepository.characters =
                listOf(
                    createCharacter(1, "Rick"),
                    createCharacter(2, "Morty"),
                )
            fakeRepository.savedRefreshResult =
                CharactersRefreshResult(
                    nextPage = 2,
                    isLastPage = false,
                )

            createViewModel()
            advanceUntilIdle()

            viewModel.loadNextPage()
            advanceUntilIdle()

            val state = viewModel.state.value

            assertTrue(state.characters.isNotEmpty())
            assertNull(state.errorMessage)
        }

    @Test
    fun `pagination error shows error message`() =
        runTest {
            fakeRepository.characters =
                listOf(
                    createCharacter(1, "Rick"),
                )
            fakeRepository.savedRefreshResult =
                CharactersRefreshResult(
                    nextPage = 2,
                    isLastPage = false,
                )

            createViewModel()
            advanceUntilIdle()

            fakeRepository.shouldThrowCharactersError = true

            viewModel.loadNextPage()
            advanceUntilIdle()

            val state = viewModel.state.value

            assertNotNull(state.errorMessage)
        }

    @Test
    fun `loadFirstPage sets loading state correctly`() =
        runTest {
            fakeRepository.characters =
                listOf(
                    createCharacter(1, "Rick"),
                )

            createViewModel()

            val stateDuringLoad = viewModel.state.value
            assertTrue(stateDuringLoad.isInitialLoading)

            advanceUntilIdle()

            val stateAfterLoad = viewModel.state.value
            assertFalse(stateAfterLoad.isInitialLoading)
        }

    @Test
    fun `requestInProgress prevents multiple requests`() =
        runTest {
            fakeRepository.characters =
                listOf(
                    createCharacter(1, "Rick"),
                )
            fakeRepository.savedRefreshResult =
                CharactersRefreshResult(
                    nextPage = 2,
                    isLastPage = false,
                )

            createViewModel()
            advanceUntilIdle()

            viewModel.loadNextPage()
            viewModel.loadNextPage()
            advanceUntilIdle()

            val state = viewModel.state.value

            assertTrue(state.characters.size == 1)
        }

    @Test
    fun `loadNextPage does nothing when last page reached`() =
        runTest {
            fakeRepository.characters =
                listOf(
                    createCharacter(1, "Rick"),
                )
            fakeRepository.savedRefreshResult =
                CharactersRefreshResult(
                    nextPage = null,
                    isLastPage = true,
                )

            createViewModel()
            advanceUntilIdle()

            viewModel.loadNextPage()
            advanceUntilIdle()

            val state = viewModel.state.value

            assertTrue(state.characters.size == 1)
        }

    @Test
    fun `last page is reached correctly`() =
        runTest {
            fakeRepository.characters =
                listOf(
                    createCharacter(1, "Rick"),
                    createCharacter(2, "Morty"),
                )
            fakeRepository.savedRefreshResult =
                CharactersRefreshResult(
                    nextPage = null,
                    isLastPage = true,
                )

            createViewModel()
            advanceUntilIdle()

            viewModel.loadNextPage()
            advanceUntilIdle()

            val state = viewModel.state.value

            assertTrue(state.endReached)
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
