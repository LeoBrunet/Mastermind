package com.example.lhmind.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.lhmind.ui.screens.CreateGameScreen
import com.example.lhmind.ui.screens.GameScreen
import com.example.lhmind.ui.screens.HomeScreen
import com.example.lhmind.ui.screens.RegisterScreen

sealed class Screen(val route: String) {
    object RegisterScreen : Screen("register")
    object CreateGameScreen : Screen("createGame")
    object Game : Screen("game")
    object HomeScreen : Screen("home")
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.RegisterScreen.route
    ) {
        composable(
            route = "home/{playerId}",
            arguments = listOf(navArgument("playerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val playerId = backStackEntry.arguments?.getLong("playerId")
            HomeScreen(
                playerId,
                navController = navController
            )
        }

        composable(Screen.RegisterScreen.route) {
            RegisterScreen { playerId ->
                navController.navigate("home/$playerId")
            }
        }

        composable(
            route = "createGame/{playerId}",
            arguments = listOf(navArgument("playerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val playerId = backStackEntry.arguments?.getLong("playerId")
            CreateGameScreen(playerId) { gameId ->
                navController.navigate("game/$gameId")
            }
        }

        composable(
            route = "game/{gameId}",
            arguments = listOf(navArgument("gameId") { type = NavType.LongType })
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getLong("gameId")
            GameScreen(gameId = gameId, navController = navController)
        }
    }
}
