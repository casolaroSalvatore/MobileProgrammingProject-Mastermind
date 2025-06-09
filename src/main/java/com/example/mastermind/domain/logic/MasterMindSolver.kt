package com.example.mastermind.domain.logic

import com.example.mastermind.domain.model.ColorPeg
import com.example.mastermind.domain.model.GameSettings

object MastermindSolver {

    fun feedback(secret: List<ColorPeg>, guess: List<ColorPeg>): Pair<Int, Int> {
        val blacks = secret.zip(guess).count { it.first == it.second }
        val secretCounts = secret.groupingBy { it }.eachCount()
        val guessCounts  = guess.groupingBy { it }.eachCount()
        val whites = secretCounts.keys.sumOf { k ->
            minOf(secretCounts[k] ?: 0, guessCounts[k] ?: 0)
        } - blacks
        return blacks to whites
    }

    fun remainingCompatible(
        settings: GameSettings,
        history: List<Pair<List<ColorPeg>, Pair<Int, Int>>>
    ): Int {
        val colors = ColorPeg.subset(settings.colors)
        fun codes(): Sequence<List<ColorPeg>> = sequence {
            val idx = IntArray(settings.codeLength)
            val last = colors.size - 1
            while (true) {
                yield(idx.map { colors[it] })
                var p = settings.codeLength - 1
                while (p >= 0 && idx[p] == last) {
                    idx[p] = 0; p--
                }
                if (p < 0) break
                idx[p]++
            }
        }.filter { settings.allowDuplicates || it.toSet().size == it.size }

        return codes().count { code ->
            history.all { (g, fb) -> feedback(code, g) == fb }
        }
    }
}
