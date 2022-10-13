package me.marizinha.pedra_papel_tesoura

import androidx.annotation.ColorRes

enum class GameResult (@ColorRes val color: Int) {
    DEFEAT(R.color.red),
    TIE(R.color.yellow),
    VICTORY(R.color.green),
}
