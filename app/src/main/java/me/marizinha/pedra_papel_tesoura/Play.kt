package me.marizinha.pedra_papel_tesoura

import androidx.annotation.DrawableRes
import java.util.*

enum class Play(@DrawableRes val icon: Int) {
    ROCK(R.drawable.ic_rock),
    PAPER(R.drawable.ic_paper),
    SCISSOR(R.drawable.ic_scissors);

    companion object {
        private val rng = Random()

        fun randomPlay(): Play {
            val plays = values()
            return plays[rng.nextInt(plays.size)]
        }
    }
}