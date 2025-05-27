package com.example.mastermind.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mastermind.R
import com.example.mastermind.viewmodel.HistoryViewModel
import java.time.format.DateTimeFormatter

@Composable
fun HistoryScreen(nav: NavHostController) {
    val vm: HistoryViewModel = viewModel()
    val games by vm.games.collectAsState()

    Column(Modifier.fillMaxSize()) {
        LazyColumn(
            Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(games) { game ->
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { nav.navigate("history/${game.id}") }
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(game.date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                        Text(stringResource(R.string.attempts_left, game.attempts))
                    }
                }
            }
        }
        Button(
            onClick = { nav.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) { Text(stringResource(R.string.back)) }
    }
}


