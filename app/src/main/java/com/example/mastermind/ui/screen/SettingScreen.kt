package com.example.mastermind.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mastermind.R
import com.example.mastermind.viewmodel.SettingViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(nav: NavHostController) {
    val vm: SettingViewModel = viewModel()
    val settings by vm.settings.collectAsState()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.title_settings)) },
                navigationIcon = {
                    IconButton(onClick = { nav.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, null)
                    }
                }
            )
        }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.pref_colors), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            ChoiceChips(
                options = listOf(6, 8, 10),
                selected = settings.colors,
                onSelect = vm::setColors
            )

            Spacer(Modifier.height(16.dp))
            Text(stringResource(R.string.pref_code_length), style = MaterialTheme.typography.titleMedium)
            ChoiceChips(
                options = listOf(4, 5),
                selected = settings.codeLength,
                onSelect = vm::setLength
            )

            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.pref_duplicates))
                Switch(
                    checked = settings.allowDuplicates,
                    onCheckedChange = vm::setDuplicates
                )
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

