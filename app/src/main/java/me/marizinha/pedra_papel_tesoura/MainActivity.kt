package me.marizinha.pedra_papel_tesoura

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import me.marizinha.pedra_papel_tesoura.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    val botPlayState = MutableLiveData<Play>()
    val playerPlayState = MutableLiveData<Play>()
    val resultState = MutableLiveData<GameResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        clickListeners()

        renderUI()
    }

    private fun renderUI() = binding.apply {
        botPlayState.observe(this@MainActivity) {
            imageBotPlay.setImageResource(it.icon)
        }

        playerPlayState.observe(this@MainActivity) {
            imagePlayerPlay.setImageResource(it.icon)
        }

        resultState.observe(this@MainActivity) {
            textResult.text = it.name
            textResult.setTextColor(ContextCompat.getColor(this@MainActivity, it.color))
        }

    }

    private fun clickListeners() = binding.apply {
        iconRock.setOnClickListener {
            playerPlayState.value = Play.ROCK
            startGame(Play.ROCK)
        }

        iconPaper.setOnClickListener {
            playerPlayState.value = Play.PAPER
            startGame(Play.PAPER)
        }

        iconScissor.setOnClickListener {
            playerPlayState.value = Play.SCISSOR
            startGame(Play.SCISSOR)
        }
    }

    private fun startGame(play: Play): GameResult {
        val botPlay = Play.randomPlay()
        botPlayState.value = botPlay

        val result = when(play) {
            Play.ROCK -> {
                if (botPlay == Play.PAPER) {
                    GameResult.DEFEAT
                } else if (botPlay == Play.ROCK) {
                    GameResult.TIE
                } else {
                    GameResult.VICTORY
                }
            }

            Play.PAPER -> {
                if (botPlay == Play.SCISSOR) {
                    GameResult.DEFEAT
                } else if (botPlay == Play.PAPER) {
                    GameResult.TIE
                } else {
                    GameResult.VICTORY
                }
            }

            Play.SCISSOR -> {
                if (botPlay == Play.ROCK) {
                    GameResult.DEFEAT
                } else if (botPlay == Play.SCISSOR) {
                    GameResult.TIE
                } else {
                    GameResult.VICTORY
                }
            }
        }

        resultState.value = result

        return result
    }
}