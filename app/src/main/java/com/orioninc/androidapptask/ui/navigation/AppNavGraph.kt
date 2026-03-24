package com.orioninc.androidapptask.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.orioninc.androidapptask.data.network.RetrofitInstance
import com.orioninc.androidapptask.data.repository.CharacterRepository
import com.orioninc.androidapptask.data.repository.CharacterRepositoryImpl
import com.orioninc.androidapptask.ui.detail.CharacterDetailScreen
import com.orioninc.androidapptask.ui.detail.CharacterDetailViewModel
import com.orioninc.androidapptask.ui.detail.CharacterDetailViewModelFactory
import com.orioninc.androidapptask.ui.list.CharacterListScreen
import com.orioninc.androidapptask.ui.list.CharacterListViewModel
import com.orioninc.androidapptask.ui.list.CharacterListViewModelFactory
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val repository: CharacterRepository = CharacterRepositoryImpl(RetrofitInstance.api)
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.CHARACTER_LIST
        ) {
            composable(NavRoutes.CHARACTER_LIST) {
                val viewModel: CharacterListViewModel = viewModel(
                    factory = CharacterListViewModelFactory(repository)
                )
                CharacterListScreen(
                    viewModel = viewModel,
                    onCharacterClick = { id ->
                        navController.navigate("${NavRoutes.CHARACTER_DETAIL}/$id")
                    },
                    animatedVisibilityScope = this@composable,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            }
            composable(
                route = "${NavRoutes.CHARACTER_DETAIL}/{${NavRoutes.CHARACTER_ID_ARG}}",
                arguments = listOf(
                    navArgument(NavRoutes.CHARACTER_ID_ARG) {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val characterId = backStackEntry.arguments?.getInt(NavRoutes.CHARACTER_ID_ARG) ?: 0
                val viewModel: CharacterDetailViewModel = viewModel(
                    factory = CharacterDetailViewModelFactory(repository, characterId)
                )
                CharacterDetailScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    animatedVisibilityScope = this@composable,
                    sharedTransitionScope = this@SharedTransitionLayout
                )
            }
        }
    }
}