package com.example.mastermind.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mastermind.R
import com.example.mastermind.ui.component.WoodBackground
import com.example.mastermind.viewmodel.HistoryViewModel
import java.time.format.DateTimeFormatter

/* Lista delle partite concluse. Tapping su un item porta a HistoryDetailScreen. */
@Composable
fun HistoryScreen(nav: NavHostController) {

    val vm: HistoryViewModel = viewModel()
    val list by vm.games.collectAsState()          // Flow<List<HistoryItem>>

    val fmt = remember {
        DateTimeFormatter.ofPattern("dd MMM yyyy – HH:mm")
    }

    WoodBackground {
        Scaffold(containerColor = Color.Transparent) { inner ->
            LazyColumn(
                modifier = Modifier
                    .padding(inner)
                    .padding(8.dp)
                    .fillMaxSize()
            ) {
                items(list) { h ->
                    ListItem(
                        headlineContent = { Text(h.date.format(fmt)) },
                        supportingContent = {
                            val result = if (h.won) "✓" else "✗"
                            Text(
                                stringResource(
                                    id = R.string.remaining_compatible,
                                    h.attempts
                                ) + " · $result"
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { nav.navigate("historyDetail/${h.id}") }
                    )
                    Divider()
                }
            }
        }
    }
}







