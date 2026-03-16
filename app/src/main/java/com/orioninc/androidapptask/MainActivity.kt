package com.orioninc.androidapptask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orioninc.androidapptask.ui.detail.CharacterDetailScreen
import com.orioninc.androidapptask.ui.list.CharacterListScreen
import com.orioninc.androidapptask.ui.theme.AndroidAppTaskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidAppTaskTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "character_list") {
                    composable("character_list") {
                        CharacterListScreen(
                            onCharacterClick = { id ->
                                navController.navigate("character_detail/$id")
                            }
                        )
                    }
                    composable("character_detail/{id}") { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id")?.toInt() ?: 0
                        CharacterDetailScreen(
                            characterId = id,
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}