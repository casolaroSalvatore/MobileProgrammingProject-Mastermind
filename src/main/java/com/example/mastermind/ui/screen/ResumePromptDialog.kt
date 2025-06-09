package com.example.mastermind.ui.screen

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.mastermind.R
import com.example.mastermind.data.entity.GameEntity
import java.time.format.DateTimeFormatter

/*
 * Dialog che chiede se riprendere la partita salvata o iniziarne
 * una nuova. Lo sfondo di legno è già disegnato dallo Screen che
 * ospita questo composable, quindi il dialog rimane trasparente.
 */

@Composable
fun ResumePromptDialog(
    game: GameEntity,
    onResume: () -> Unit,
    onDiscard: () -> Unit
) {
    val fmt = DateTimeFormatter.ofPattern("dd MMM yyyy – HH:mm")
    val msg = stringResource(
        id = R.string.dialog_resume_message,
        game.moves.size,
        game.date.format(fmt)
    )

    AlertDialog(
        onDismissRequest = {},                       // Scelta obbligatoria
        title = { Text(stringResource(R.string.dialog_resume_title)) },
        text  = { Text(msg) },
        confirmButton = {
            TextButton(onClick = onResume) {
                Text(stringResource(R.string.dialog_resume_resume))
            }
        },
        dismissButton = {
            TextButton(onClick = onDiscard) {
                Text(stringResource(R.string.dialog_resume_new))
            }
        },
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = .95f)
    )
}
