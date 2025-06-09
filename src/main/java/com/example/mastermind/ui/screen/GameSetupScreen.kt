package com.example.mastermind.ui.screen

import androidx.compose.foundation.layout.*
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mastermind.R
import com.example.mastermind.data.entity.GameEntity
import com.example.mastermind.domain.model.GameSettings
import com.example.mastermind.ui.component.OptionGroup               // <— nuovo
import com.example.mastermind.ui.component.WoodBackground
import com.example.mastermind.viewmodel.GameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSetupScreen(nav: NavHostController) {

    val vm: GameViewModel = viewModel()
    val current by vm.settings.collectAsState()

    /* Stato locale delle scelte */
    var colors       by remember { mutableStateOf(current.colors.coerceIn(6, 10)) }
    var length       by remember { mutableStateOf(current.codeLength.coerceIn(4, 5)) }
    var duplicates   by remember { mutableStateOf(current.allowDuplicates) }
    var attempts     by remember { mutableStateOf(current.maxAttempts) }

    /* Verifica salvataggio partita incompleta ----------------------- */
    var savedGame by remember { mutableStateOf<GameEntity?>(null) }
    var showDlg   by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        savedGame = vm.getOngoingSummary()
        showDlg   = savedGame != null
    }

    val scope = rememberCoroutineScope()

    savedGame?.let { g ->
        if (showDlg) ResumePromptDialog(
            game = g,
            onResume = {
                scope.launch { vm.loadOngoing() }
                nav.navigate("game")
            },
            onDiscard = {
                vm.discardOngoing(g.id)
                showDlg = false
            }
        )
    }

    /* ---- UI ------------------------------------------------------------ */
    WoodBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                LargeTopAppBar(
                    title = { Text(stringResource(R.string.dialog_title_setup)) },
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
            Column(
                Modifier
                    .padding(inner)
                    .padding(horizontal = 24.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                /* Gruppi di bottoni per le scelte discrete ---- */
                OptionGroup(
                    label     = "Colori",
                    options   = listOf(6, 8, 10),
                    selected  = colors,
                    onSelect  = { colors = it }
                )

                OptionGroup(
                    label     = "Lunghezza codice",
                    options   = listOf(4, 5),
                    selected  = length,
                    onSelect  = { length = it }
                )

                OptionGroup(
                    label     = "Ripetizioni",
                    options   = listOf(true, false),
                    selected  = duplicates,
                    toString  = { if (it) "Sì" else "No" },
                    onSelect  = { duplicates = it }
                )

                /* Se vuoi ancora far scegliere il numero di tentativi,
                   lascia il vecchio NumberStepper o uno slider */
                /*
                NumberStepper(
                    label   = "Tentativi",
                    value   = attempts,
                    range   = 8..15,
                    onChange = { attempts = it }
                )
                */

                Spacer(Modifier.height(32.dp))

                Button(
                    onClick = {
                        vm.startNewGame(
                            GameSettings(colors, length, duplicates, attempts)
                        )
                        nav.navigate("game")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(stringResource(R.string.start)) }
            }
        }
    }
}

