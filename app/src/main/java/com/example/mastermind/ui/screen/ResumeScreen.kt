package com.example.mastermind.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mastermind.R
import com.example.mastermind.viewmodel.GameViewModel

@Composable
fun ResumeScreen(nav: NavHostController) {
    val vm: GameViewModel = viewModel()
    val scope = rememberCoroutineScope()

    var hasGame by remember { mutableStateOf<Boolean?>(null) }

    LaunchedEffect(Unit) {
        hasGame = vm.loadOngoing()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (hasGame) {
            null -> CircularProgressIndicator()
            true -> {
                Text(stringResource(R.string.found_saved_game))
                Spacer(Modifier.height(16.dp))
                Button(onClick = { nav.navigate("game") }) {
                    Text(stringResource(R.string.continue_game))
                }
            }
            false -> {
                Text(stringResource(R.string.no_saved_game))
                Spacer(Modifier.height(16.dp))
                Button(onClick = { nav.popBackStack() }) {
                    Text(stringResource(R.string.back))
                }
            }
        }
    }
}

