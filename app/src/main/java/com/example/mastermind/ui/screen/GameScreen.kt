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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mastermind.R
import com.example.mastermind.domain.model.ColorPeg
import com.example.mastermind.ui.component.BoardRow
import com.example.mastermind.ui.component.GuessEditor
import com.example.mastermind.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(nav: NavHostController) {
    val vm: GameViewModel = viewModel()

    val moves      by vm.moves.collectAsState()
    val remaining  by vm.remaining.collectAsState()
    val editing    by vm.editing.collectAsState()
    val canSubmit  by vm.canSubmit.collectAsState(initial = false)
    val settings   by vm.settings.collectAsState()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.title_game)) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = { vm.saveProgress() }) {
                        Icon(Icons.Default.Save, contentDescription = null)
                    }
                }
            )
        }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            /* ---------- board ---------- */
            LazyColumn(
                Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                reverseLayout = true
            ) {
                items(moves.reversed()) { m ->
                    BoardRow(m.guess, m.blacks, m.whites)
                }
            }

            /* ---------- compatibili ---------- */
            remaining?.let {
                AssistChip(
                    onClick = {},
                    label = { Text(stringResource(R.string.remaining_compatible, it)) },
                    enabled = false,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Spacer(Modifier.height(8.dp))

            /* ---------- editor ---------- */
            GuessEditor(
                current = editing,
                palette = ColorPeg.subset(settings.colors),
                allowDuplicates = settings.allowDuplicates,
                onChange = { list -> list.forEachIndexed { i, c -> vm.updatePeg(i, c) } },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            /* ---------- submit + new ---------- */
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilledTonalButton(
                    onClick = { vm.startNewGame() },
                    modifier = Modifier.weight(1f)
                ) { Text(stringResource(R.string.menu_new_game)) }

                Button(
                    onClick = { vm.commitGuess() },
                    enabled = canSubmit,
                    modifier = Modifier.weight(1f)
                ) { Text(stringResource(R.string.submit_guess)) }
            }
        }
    }
}



