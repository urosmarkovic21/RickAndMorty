package com.orioninc.androidapptask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.orioninc.androidapptask.ui.navigation.AppNavGraph
import com.orioninc.androidapptask.ui.theme.AndroidAppTaskTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidAppTaskTheme {
                AppNavGraph()
            }
        }
    }
}