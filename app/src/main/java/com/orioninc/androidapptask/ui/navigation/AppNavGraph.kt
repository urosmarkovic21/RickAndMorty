package com.orioninc.androidapptask.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.orioninc.androidapptask.data.local.DatabaseProvider
import com.orioninc.androidapptask.data.network.RetrofitInstance
import com.orioninc.androidapptask.data.repository.CharacterRepositoryImpl
import com.orioninc.androidapptask.domain.GetCharacterUseCase
import com.orioninc.androidapptask.domain.GetCharactersUseCase
import com.orioninc.androidapptask.domain.GetSavedCharactersRefreshResultUseCase
import com.orioninc.androidapptask.domain.RefreshCharacterUseCase
import com.orioninc.androidapptask.domain.RefreshCharactersUseCase
import com.orioninc.androidapptask.ui.detail.CharacterDetailScreen
import com.orioninc.androidapptask.ui.detail.CharacterDetailViewModel
import com.orioninc.androidapptask.ui.detail.CharacterDetailViewModelFactory
import com.orioninc.androidapptask.ui.list.CharacterListScreen
import com.orioninc.androidapptask.ui.list.CharacterListViewModel
import com.orioninc.androidapptask.ui.list.CharacterListViewModelFactory
import com.orioninc.androidapptask.util.NotificationHelper

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current.applicationContext

    val database = remember { DatabaseProvider.getDatabase(context) }
    val dao = database.characterDao()
    val pageInfoDao = database.characterInfoDao()

    val repository =
        CharacterRepositoryImpl(
            api = RetrofitInstance.api,
            dao = dao,
            infoDao = pageInfoDao,
        )
    val notificationHelper = remember { NotificationHelper(context) }
    val getCharactersUseCase = remember { GetCharactersUseCase(repository) }
    val refreshCharactersUseCase = remember { RefreshCharactersUseCase(repository) }

    val getSavedCharactersRefreshResultUseCase =
        remember { GetSavedCharactersRefreshResultUseCase(repository) }
    val getCharacterUseCase = remember { GetCharacterUseCase(repository) }
    val refreshCharacterUseCase = remember { RefreshCharacterUseCase(repository) }
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.CHARACTER_LIST,
        ) {
            composable(NavRoutes.CHARACTER_LIST) {
                val viewModel: CharacterListViewModel =
                    viewModel(
                        factory =
                            CharacterListViewModelFactory(
                                getCharactersUseCase = getCharactersUseCase,
                                refreshCharactersUseCase = refreshCharactersUseCase,
                                getSavedCharactersRefreshResultUseCase =
                                getSavedCharactersRefreshResultUseCase,
                                notificationHelper = notificationHelper,
                            ),
                    )
                CharacterListScreen(
                    viewModel = viewModel,
                    onCharacterClick = { id ->
                        navController.navigate("${NavRoutes.CHARACTER_DETAIL}/$id")
                    },
                    animatedVisibilityScope = this@composable,
                    sharedTransitionScope = this@SharedTransitionLayout,
                )
            }
            composable(
                route = "${NavRoutes.CHARACTER_DETAIL}/{${NavRoutes.CHARACTER_ID_ARG}}",
                arguments =
                    listOf(
                        navArgument(NavRoutes.CHARACTER_ID_ARG) {
                            type = NavType.IntType
                        },
                    ),
            ) { backStackEntry ->
                val characterId =
                    backStackEntry.arguments?.getInt(NavRoutes.CHARACTER_ID_ARG) ?: 0
                val viewModel: CharacterDetailViewModel =
                    viewModel(
                        factory =
                            CharacterDetailViewModelFactory(
                                getCharacterUseCase = getCharacterUseCase,
                                refreshCharacterUseCase = refreshCharacterUseCase,
                                characterId = characterId,
                            ),
                    )
                CharacterDetailScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    animatedVisibilityScope = this@composable,
                    sharedTransitionScope = this@SharedTransitionLayout,
                )
            }
        }
    }
}
