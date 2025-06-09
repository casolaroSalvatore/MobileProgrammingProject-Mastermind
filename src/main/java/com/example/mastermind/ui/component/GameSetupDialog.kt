package com.example.mastermind.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mastermind.R
import com.example.mastermind.domain.model.GameSettings

@Composable
fun GameSetupDialog(
    initial: GameSettings,
    onConfirm: (GameSettings) -> Unit,
    onDismiss: () -> Unit
) {
    var colors        by remember { mutableStateOf(initial.colors) }
    var length        by remember { mutableStateOf(initial.codeLength) }
    var duplicates    by remember { mutableStateOf(initial.allowDuplicates) }
    var attempts      by remember { mutableStateOf(initial.maxAttempts) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm(
                    GameSettings(colors, length, duplicates, attempts)
                )
            }) { Text(stringResource(R.string.start)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        },
        title = { Text(stringResource(R.string.dialog_title_setup)) },
        text  = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Grandezza della palette di colori
                Text(stringResource(R.string.label_colors, colors))
                Slider(
                    value = colors.toFloat(),
                    onValueChange = { colors = it.toInt() },
                    valueRange = 6f..10f, steps = 2
                )
                // Lunghezza del codice
                Text(stringResource(R.string.label_length, length))
                Slider(
                    value = length.toFloat(),
                    onValueChange = { length = it.toInt() },
                    valueRange = 4f..6f, steps = 2
                )
                // Tentativi massimi
                Text(stringResource(R.string.label_attempts, attempts))
                Slider(
                    value = attempts.toFloat(),
                    onValueChange = { attempts = it.toInt() },
                    valueRange = 8f..15f, steps = 7
                )
                // Duplicati
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = duplicates, onCheckedChange = { duplicates = it })
                    Spacer(Modifier.width(4.dp))
                    Text(stringResource(R.string.label_duplicates))
                }
            }
        }
    )
}
