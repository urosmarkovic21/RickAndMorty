package com.orioninc.androidapptask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import coil.ImageLoader
import com.orioninc.androidapptask.ui.navigation.AppNavGraph
import com.orioninc.androidapptask.ui.theme.AndroidAppTaskTheme
import coil.Coil
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setOnExitAnimationListener { splashScreenView ->
            splashScreenView.view.animate()
                .alpha(0f)
                .setDuration(5000L)
                .withEndAction {
                    splashScreenView.remove()
                }
                .start()
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidAppTaskTheme {
                AppNavGraph()
            }
        }
    }
}