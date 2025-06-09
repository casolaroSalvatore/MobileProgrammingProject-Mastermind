package com.example.mastermind

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mastermind.service.BackgroundMusicService
import com.example.mastermind.ui.screen.*
import com.example.mastermind.ui.theme.MastermindTheme
import com.example.mastermind.ui.component.WoodBackground

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Avvia la musica di sottofondo
        startService(Intent(this, BackgroundMusicService::class.java))

        setContent {
            MastermindTheme {
                // Applica lo sfondo in legno a tutto il contenuto
                WoodBackground {
                    val nav = rememberNavController()

                    NavHost(navController = nav, startDestination = "menu") {
                        composable("menu")     { MenuScreen(nav) }
                        composable("setup")    { GameSetupScreen(nav) }
                        composable("game")     { GameScreen(nav) }
                        composable("resume")   { ResumeScreen(nav) }
                        composable("settings") { SettingScreen(nav) }
                        composable("history")  { HistoryScreen(nav) }
                        composable(
                            "history/{gameId}",
                            arguments = listOf(navArgument("gameId") {
                                type = NavType.LongType
                            })
                        ) { back ->
                            val id = back.arguments!!.getLong("gameId")
                            HistoryDetailScreen(id, nav)
                        }
                    }
                }
            }
        }
    }
}

