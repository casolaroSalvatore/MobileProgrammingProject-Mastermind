package com.example.mastermind.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mastermind.domain.model.ColorPeg

/* Peg arrotondato con leggero drop-shadow */
@Composable
fun Peg(color: ColorPeg, size: Int = 28) = Box(
    Modifier
        .size(size.dp)
        .shadow(4.dp, CircleShape, clip = false)
        .clip(CircleShape)
        .background(Color(color.argb))
)
