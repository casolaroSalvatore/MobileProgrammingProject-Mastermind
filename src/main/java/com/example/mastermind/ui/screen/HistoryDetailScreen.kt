package com.example.mastermind.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mastermind.R
import com.example.mastermind.data.MastermindDatabase
import com.example.mastermind.data.repository.GameRepository
import com.example.mastermind.domain.model.ColorPeg
import com.example.mastermind.ui.component.BoardRow
import com.example.mastermind.ui.component.Peg
import com.example.mastermind.ui.component.WoodBackground
import com.example.mastermind.viewmodel.HistoryDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(
    gameId: Long,
    nav: NavHostController
) {
    val context = LocalContext.current.applicationContext
    val vm: HistoryDetailViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo = GameRepository(MastermindDatabase.get(context).gameDao())
                @Suppress("UNCHECKED_CAST")
                return HistoryDetailViewModel(repo, gameId) as T
            }
        }
    )

    val rows by vm.rows.collectAsState()
    val secret by vm.secret.collectAsState()
    val won by vm.won.collectAsState()
    val date by vm.formattedDate.collectAsState()

    WoodBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                SmallTopAppBar(
                    title = { Text(stringResource(R.string.history_detail_title)) },
                    navigationIcon = {
                        IconButton(onClick = { nav.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, null)
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { inner ->

            Column(
                Modifier
                    .padding(inner)
                    .padding(16.dp)
            ) {
                Text(date, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(12.dp))

                if (!won) {
                    Text(stringResource(R.string.label_secret_code))
                    Spacer(Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        secret.forEach { Peg(it, 28) }
                    }
                    Spacer(Modifier.height(12.dp))
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(rows) { m ->
                        BoardRow(m.guess, m.blacks, m.whites)
                    }
                }
            }
        }
    }
}




