package com.example.mastermind.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mastermind.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(nav: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(R.string.app_name)) })
        }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MenuButton(
                icon = Icons.Default.PlayArrow,
                label = stringResource(R.string.title_game)
            ) { nav.navigate("game") }

            MenuButton(
                icon = Icons.Default.Storage,
                label = stringResource(R.string.title_resume)
            ) { nav.navigate("resume") }

            MenuButton(
                icon = Icons.Default.History,
                label = stringResource(R.string.title_history)
            ) { nav.navigate("history") }

            MenuButton(
                icon = Icons.Default.Settings,
                label = stringResource(R.string.title_settings)
            ) { nav.navigate("settings") }
        }
    }
}

@Composable
private fun MenuButton(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.75f)
            .padding(vertical = 8.dp)
    ) {
        Row(
            Modifier
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null)
            Text(label, style = MaterialTheme.typography.titleLarge)
        }
    }
}

