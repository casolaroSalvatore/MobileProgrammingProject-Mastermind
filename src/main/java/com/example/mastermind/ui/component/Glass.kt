package com.example.mastermind.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/* Superficie sfocata e semi-trasparente che lascia intravedere il legno */
@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    blurRadius: Int = 8,
    alpha: Float = 0.60f,
    content: @Composable () -> Unit
) = Box(
    modifier
        .fillMaxSize()
        .background(Color.White.copy(alpha = alpha))
        .blur(blurRadius.dp)
) { content() }

/* Card “vetro” per pannelli locali (dialog, row, ecc.) */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) = Card(
    modifier
        .clip(RoundedCornerShape(20.dp))
        .shadow(8.dp, RoundedCornerShape(20.dp), clip = false)
        .blur(6.dp),
    colors    = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.55f)),
    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
    content   = content
)