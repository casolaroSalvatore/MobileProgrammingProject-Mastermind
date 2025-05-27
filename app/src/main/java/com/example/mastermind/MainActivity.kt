package com.example.mastermind

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mastermind.ui.screen.GameScreen
import com.example.mastermind.ui.screen.HistoryDetailScreen
import com.example.mastermind.ui.screen.HistoryScreen
import com.example.mastermind.ui.screen.MenuScreen
import com.example.mastermind.ui.screen.ResumeScreen
import com.example.mastermind.ui.screen.SettingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MastermindApp() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MastermindApp() {
    val nav = rememberNavController()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) }
            )
        }
    ) { inner ->
        NavHost(
            navController = nav,
            startDestination = "menu",
            modifier = Modifier.padding(inner)
        ) {
            composable("menu")     { MenuScreen(nav) }
            composable("game")     { GameScreen(nav) }
            composable("resume")   { ResumeScreen(nav) }
            composable("settings") { SettingScreen(nav) }
            composable("history")  { HistoryScreen(nav) }
            composable(
                "history/{gameId}",
                arguments = listOf(navArgument("gameId") { type = NavType.LongType })
            ) { backStackEntry ->
                val id = backStackEntry.arguments!!.getLong("gameId")
                HistoryDetailScreen(id, nav)
            }
        }
    }
}
