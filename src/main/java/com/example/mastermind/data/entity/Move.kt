package com.example.mastermind.data.entity

import com.example.mastermind.domain.model.ColorPeg
import java.io.Serializable

data class Move(
    val guess:  List<ColorPeg>,
    val blacks: Int,
    val whites: Int
) : Serializable