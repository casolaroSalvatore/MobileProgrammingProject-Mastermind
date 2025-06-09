package com.example.mastermind.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mastermind.R
import com.example.mastermind.domain.model.ColorPeg
import com.example.mastermind.ui.component.BoardRow
import com.example.mastermind.ui.component.EndOverlay
import com.example.mastermind.ui.component.GuessEditor
import com.example.mastermind.ui.component.WoodBackground
import com.example.mastermind.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(nav: NavHostController) {

    val vm: GameViewModel = viewModel()

    val moves      by vm.moves.collectAsState()
    val remaining  by vm.remaining.collectAsState()
    val editing    by vm.editing.collectAsState()
    val canSubmit  by vm.canSubmit.collectAsState(false)
    val settings   by vm.settings.collectAsState()
    val status     by vm.status.collectAsState()

    var askExit by remember { mutableStateOf(false) }

    WoodBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                LargeTopAppBar(
                    title = { Text(stringResource(R.string.title_game)) },
                    navigationIcon = {
                        IconButton(onClick = { askExit = true }) {
                            Icon(Icons.Default.ArrowBack, null)
                        }
                    },
                    actions = {
                        IconButton(onClick = vm::saveProgress) {
                            Icon(Icons.Default.Save, null)
                        }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { inner ->

            /* -----------------   BODY   ----------------- */
            Box(
                Modifier
                    .padding(inner)
                    .fillMaxSize()
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    /* Storico tentativi */
                    LazyColumn(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        items(moves.reversed()) { m ->
                            BoardRow(m.guess, m.blacks, m.whites)
                        }
                    }

                    remaining?.let {
                        AssistChip(
                            onClick = {},
                            label = {
                                Text(stringResource(R.string.remaining_compatible, it))
                            },
                            enabled = false,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }

                    Spacer(Modifier.height(8.dp))

                    /* Editor tentativo corrente */
                    GuessEditor(
                        current          = editing,
                        palette          = ColorPeg.subset(settings.colors),
                        allowDuplicates  = settings.allowDuplicates,
                        onChange         = { list ->
                            list.forEachIndexed { i, c -> vm.updatePeg(i, c) }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    /* Pulsanti INVIA e CANCELLA */
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = vm::commitGuess,
                            enabled = canSubmit,
                            modifier = Modifier.weight(1f)
                        ) { Text(stringResource(R.string.btn_submit)) }

                        OutlinedButton(
                            onClick = { vm.updatePeg(-1, null) },
                            modifier = Modifier.weight(1f)
                        ) { Text(stringResource(R.string.btn_clear)) }
                        }
                    }
                }

                /* Overlay di fine partita */
                EndOverlay(
                    status       = status,
                    onNewGame    = { nav.navigate("setup") },
                    onBackToMenu = { nav.navigate("menu") { popUpTo("menu") { inclusive = true } } }
                )
            }
        }

        /* ---------------   DIALOG USCITA   --------------- */
        if (askExit) AlertDialog(
            onDismissRequest = { askExit = false },
            title = { Text(stringResource(R.string.dialog_exit_title)) },
            text  = { Text(stringResource(R.string.dialog_exit_message)) },
            confirmButton = {
                TextButton(onClick = {
                    vm.saveProgress()
                    nav.navigate("menu") { popUpTo("menu") { inclusive = true } }
                }) { Text(stringResource(R.string.dialog_exit_save_exit)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    vm.abandonWithoutSaving()
                    nav.navigate("menu") { popUpTo("menu") { inclusive = true } }
                }) { Text(stringResource(R.string.dialog_exit_exit)) }
            },
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = .95f)
        )
    }





