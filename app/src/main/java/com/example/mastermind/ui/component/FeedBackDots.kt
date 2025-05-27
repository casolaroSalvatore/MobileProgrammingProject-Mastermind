package com.example.mastermind.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mastermind.ui.theme.PegFeedback

/* Visualizza fino a 5 pallini feedback (neri/white outline). */
@Composable
fun FeedbackDots(blacks: Int, whites: Int, codeLength: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(blacks) {
            Dot(Color.Black)
        }
        repeat(whites) {
            Dot(Color.White, border = PegFeedback)
        }
        repeat(codeLength - blacks - whites) {
            Dot(Color.Transparent)
        }
    }
}

@Composable
private fun Dot(color: Color, border: Color = Color.Transparent) {
    Box(
        modifier = Modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(color = color)
            .border(1.dp, border, CircleShape)
    )
}
