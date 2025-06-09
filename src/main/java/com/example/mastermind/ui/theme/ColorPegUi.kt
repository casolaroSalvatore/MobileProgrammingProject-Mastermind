package com.example.mastermind.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.mastermind.domain.model.ColorPeg

/* Mapping molto semplice enum → colore Material. */
val ColorPeg.uiColor: Color
    get() = when (this) {
        ColorPeg.RED     -> Color(0xFFE53935)
        ColorPeg.GREEN   -> Color(0xFF43A047)
        ColorPeg.BLUE    -> Color(0xFF1E88E5)
        ColorPeg.YELLOW  -> Color(0xFFFDD835)
        ColorPeg.ORANGE  -> Color(0xFFFB8C00)
        ColorPeg.PURPLE  -> Color(0xFF8E24AA)
        ColorPeg.CYAN    -> Color(0xFF00ACC1)
        ColorPeg.MAGENTA -> Color(0xFFD81B60)
        ColorPeg.BROWN   -> Color(0xFF8D6E63)
        ColorPeg.BLACK   -> Color(0xFF212121)
    }
