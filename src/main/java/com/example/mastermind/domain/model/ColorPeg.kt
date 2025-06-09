package com.example.mastermind.domain.model

// Ogni colore porta con sé il proprio valore ARGB (32 bit) usato da Jetpack Compose.

enum class ColorPeg(val argb: Int) {
    RED     (0xFFFF0000.toInt()),
    GREEN   (0xFF4CAF50.toInt()),
    BLUE    (0xFF2196F3.toInt()),
    YELLOW  (0xFFFFEB3B.toInt()),
    ORANGE  (0xFFFF9800.toInt()),
    PURPLE  (0xFF9C27B0.toInt()),
    CYAN    (0xFF00BCD4.toInt()),
    MAGENTA (0xFFE91E63.toInt()),
    BROWN   (0xFF795548.toInt()),
    BLACK   (0xFF000000.toInt());

    companion object {
        // Restituisce i primi *count* colori della palette.
        fun subset(count: Int): List<ColorPeg> = ColorPeg.entries.toTypedArray().take(count)
    }
}

