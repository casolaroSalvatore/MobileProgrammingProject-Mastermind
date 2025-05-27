package com.example.mastermind.domain.model

data class GameSettings(
    val colors: Int = 6,          // 6, 8, 10
    val codeLength: Int = 4,      // 4 o 5
    val allowDuplicates: Boolean = false
)
