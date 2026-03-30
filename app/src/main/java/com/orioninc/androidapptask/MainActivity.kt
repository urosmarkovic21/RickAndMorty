package com.orioninc.androidapptask

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.orioninc.androidapptask.ui.navigation.AppNavGraph
import com.orioninc.androidapptask.ui.theme.AndroidAppTaskTheme
import com.orioninc.androidapptask.util.NotificationHelper

class MainActivity : ComponentActivity() {
    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setOnExitAnimationListener { splashScreenView ->
            splashScreenView.view
                .animate()
                .alpha(0f)
                .setDuration(5000L)
                .withEndAction {
                    splashScreenView.remove()
                }.start()
        }
        super.onCreate(savedInstanceState)
        val notificationHelper = NotificationHelper(this)
        notificationHelper.createNotificationChannel()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted =
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
        enableEdgeToEdge()

        setContent {
            AndroidAppTaskTheme {
                AppNavGraph()
            }
        }
    }
}
