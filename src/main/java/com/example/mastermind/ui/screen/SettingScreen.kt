package com.example.mastermind.ui.screen

import androidx.compose.foundation.layout.*
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
import com.example.mastermind.ui.component.WoodBackground
import com.example.mastermind.viewmodel.SettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(nav: NavHostController) {
    val vm: SettingViewModel = viewModel()
    val musicVol by vm.musicVolume.collectAsState()
    val sfxVol by vm.sfxVolume.collectAsState()

    WoodBackground {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                LargeTopAppBar(
                    title = { Text(stringResource(R.string.title_settings)) },
                    navigationIcon = { IconButton({ nav.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null) } },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { inner ->
            Column(
                Modifier.padding(inner).padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                // Sezione AUDIO
                Text(
                    stringResource(R.string.section_audio),
                    style = MaterialTheme.typography.titleMedium
                )

                /* Musica di sottofondo */
                Text(stringResource(R.string.music_volume, (musicVol * 100).toInt()))
                Slider(
                    value = musicVol,
                    onValueChange = vm::setMusicVolume,
                    valueRange = 0f..1f
                )

                /* Effetti sonori */
                Text(stringResource(R.string.sfx_volume, (sfxVol * 100).toInt()))
                Slider(
                    value = sfxVol,
                    onValueChange = vm::setSfxVolume,
                    valueRange = 0f..1f
                )

                /* spazio per future impostazioni (tema, vibrazione, …) */

            }
        }
    }
}

@Composable
private fun ChoiceChips(options: List<Int>, selected: Int, onSelect: (Int) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { num ->
            FilterChip(
                selected = num == selected,
                onClick = { onSelect(num) },
                label = { Text(num.toString()) }
            )
        }
    }
}

