package com.example.mastermind.ui.component

import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.mastermind.R
import com.example.mastermind.domain.model.GameStatus

/* Overlay semi-trasparente che appare sopra la GameScreen
   quando la partita è finita (vittoria o sconfitta). */

@Composable
fun EndOverlay(
    status: GameStatus,
    onNewGame: () -> Unit,
    onBackToMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (status == GameStatus.Playing) return

    val ctx = LocalContext.current

    // Jingle una sola volta
    LaunchedEffect(status) {
        val res = when (status) {
            GameStatus.Won     -> R.raw.victory
            is GameStatus.Lost -> R.raw.defeat
            else               -> null
        }
        res?.let {
            MediaPlayer.create(ctx, it).apply {
                start()
                setOnCompletionListener { release() }
            }
        }
    }

    // UI
    Box(
        modifier
            .fillMaxSize()
            .zIndex(1f)
            .background(Color.Black.copy(alpha = .55f))
            .clickable(enabled = false) {}
    ) {
        Card(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp),
            shape  = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(12.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                when (status) {
                    GameStatus.Won -> {
                        Text(stringResource(R.string.win_title),
                            style = MaterialTheme.typography.headlineMedium)
                        Spacer(Modifier.height(8.dp))
                        Text(stringResource(R.string.win_message),
                            textAlign = TextAlign.Center)
                    }
                    is GameStatus.Lost -> {
                        Text(stringResource(R.string.lose_title),
                            style = MaterialTheme.typography.headlineMedium)
                        Spacer(Modifier.height(8.dp))
                        Text(stringResource(R.string.lose_message),
                            textAlign = TextAlign.Center)
                        Spacer(Modifier.height(12.dp))
                        // Pallini segreti
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            status.secret.forEach { Peg(it, 32) }
                        }
                    }
                    else -> {}
                }

                Spacer(Modifier.height(24.dp))
                Button(onClick = onNewGame, Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.menu_new_game))
                }
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = onBackToMenu, Modifier.fillMaxWidth()) {
                    Text(stringResource(R.string.back_to_menu))
                }
            }
        }
    }
}
