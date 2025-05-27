package com.example.mastermind.domain.model

enum class ColorPeg {
    RED, GREEN, BLUE, YELLOW, ORANGE, PURPLE, CYAN, MAGENTA, BROWN, BLACK;

    companion object {
        fun subset(count: Int) = values().take(count)
    }
}
