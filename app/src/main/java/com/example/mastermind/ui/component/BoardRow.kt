package com.example.mastermind.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.mastermind.domain.model.ColorPeg
import com.example.mastermind.ui.theme.PegBorder
import com.example.mastermind.ui.theme.uiColor

@Composable
fun BoardRow(
    guess: List<ColorPeg>,
    blacks: Int,
    whites: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        tonalElevation = 1.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                guess.forEach {
                    Box(
                        Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(it.uiColor)
                            .border(1.dp, PegBorder, CircleShape)
                    )
                }
            }
            FeedbackDots(blacks, whites, guess.size)
        }
    }
    Spacer(Modifier.height(6.dp))
}

