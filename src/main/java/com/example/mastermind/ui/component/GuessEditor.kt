package com.example.mastermind.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mastermind.domain.model.ColorPeg
import com.example.mastermind.ui.theme.PegBorder
import com.example.mastermind.ui.theme.uiColor

/* Riga di peg + palette sottostante */
@Composable
fun GuessEditor(
    current: List<ColorPeg?>,
    palette: List<ColorPeg>,
    allowDuplicates: Boolean,
    onChange: (List<ColorPeg?>) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier.animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        /* ---------- Peg editabili ---------- */
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            current.forEachIndexed { i, peg ->
                Peg(
                    color = peg?.uiColor ?: Color.Transparent,
                    empty = peg == null,
                    selected = i == selectedIndex,
                    onClick = { selectedIndex = i }
                )
            }
        }

        /* ---------- Palette ---------- */
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            palette.forEach { pegColor ->
                Box(
                    Modifier
                        .size(32.dp)
                        .shadow(2.dp, CircleShape, clip = false)
                        .clip(CircleShape)
                        .background(pegColor.uiColor)
                        .clickable(enabled = selectedIndex != null) {
                            selectedIndex?.let { idx ->
                                val list = current.toMutableList()
                                if (!allowDuplicates && pegColor in list) return@clickable
                                list[idx] = pegColor
                                onChange(list)
                            }
                        }
                )
            }
        }
    }
}

@Composable
private fun Peg(color: Color, empty: Boolean, selected: Boolean, onClick: () -> Unit) {
    val outline = if (selected) MaterialTheme.colorScheme.primary else PegBorder
    Box(
        modifier = Modifier
            .size(48.dp)
            .border(2.dp, outline, CircleShape)
            .background(color, CircleShape)
            .alpha(if (empty) .15f else 1f)
            .clickable(onClick = onClick)
    )
}

