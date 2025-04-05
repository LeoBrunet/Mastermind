package com.example.lhmind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.lhmind.ui.navigation.Navigation
import com.example.lhmind.ui.theme.LHMindTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LHMindTheme {
                val navController = rememberNavController()
                Navigation(navController = navController)
            }
        }
    }
}
