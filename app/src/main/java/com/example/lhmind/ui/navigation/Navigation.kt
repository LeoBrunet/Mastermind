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
import com.example.lhmind.ui.screens.InvitationsScreen

@Suppress("unused")
sealed class Screen(val route: String) {
    data object RegisterScreen : Screen("register")
    data object CreateGameScreen : Screen("createGame")
    data object Game : Screen("game")
    data object HomeScreen : Screen("home")
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.RegisterScreen.route
    ) {
        composable(Screen.RegisterScreen.route) {
            RegisterScreen { playerId ->
                navController.navigate("home/$playerId")
            }
        }

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

        composable(
            route = "invitations/{playerId}",
            arguments = listOf(navArgument("playerId") { type = NavType.LongType })
        ) {
            InvitationsScreen(
                navController = navController
            )
        }

        composable(
            route = "createGame/{playerId}",
            arguments = listOf(navArgument("playerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val playerId = backStackEntry.arguments?.getLong("playerId")
            CreateGameScreen(playerId) { gameId ->
                navController.navigate("game/$gameId/$playerId")
            }
        }

        composable(
            route = "game/{gameId}/{playerId}",
            arguments = listOf(
                navArgument("gameId") { type = NavType.LongType },
                navArgument("playerId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getLong("gameId")
            val playerId = backStackEntry.arguments?.getLong("playerId")
            GameScreen(gameId = gameId, playerId = playerId, navController = navController)
        }
    }
}
