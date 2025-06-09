package com.example.mastermind.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mastermind.R
import com.example.mastermind.ui.component.BoardRow
import com.example.mastermind.viewmodel.GameViewModel
import com.example.mastermind.ui.component.WoodBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumeScreen(nav: NavHostController) {

    val vm: GameViewModel = viewModel()
    val moves by vm.moves.collectAsState()

    WoodBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                LargeTopAppBar(
                    title = { Text(stringResource(R.string.title_resume)) },
                    navigationIcon = {
                        IconButton(onClick = { nav.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, null)
                        }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { inner ->
            LazyColumn(
                Modifier
                    .padding(inner)
                    .fillMaxSize()
            ) {
                items(moves) { m ->
                    BoardRow(m.guess, m.blacks, m.whites)
                }
            }
        }
    }
}


