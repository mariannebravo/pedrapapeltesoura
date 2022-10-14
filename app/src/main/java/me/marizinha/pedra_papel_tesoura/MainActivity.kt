package me.marizinha.pedra_papel_tesoura

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.marizinha.pedra_papel_tesoura.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val botPlayState = MutableLiveData<Play>()
    private val playerPlayState = MutableLiveData<Play>()
    private val resultState = MutableLiveData<GameResult>()
    private val score = MutableLiveData(Pair(0, 0)) // Pair<BotScore, PlayerScore>
    private val play = MutableLiveData(1)

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

        score.observe(this@MainActivity) {
            textBot.text = getString(R.string.bot_score, it.first)
            textYou.text = getString(R.string.you_score, it.second)
        }

        play.observe(this@MainActivity) {
            if (it > 1) {
                textPlays.visibility = View.VISIBLE
            }

            textPlays.text = getString(R.string.plays, it, MAX_PLAYS)
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

        val result = when (play) {
            Play.ROCK -> {
                when (botPlay) {
                    Play.PAPER -> {
                        GameResult.DEFEAT
                    }
                    Play.ROCK -> {
                        GameResult.TIE
                    }
                    else -> {
                        GameResult.VICTORY
                    }
                }
            }

            Play.PAPER -> {
                when (botPlay) {
                    Play.SCISSOR -> {
                        GameResult.DEFEAT
                    }
                    Play.PAPER -> {
                        GameResult.TIE
                    }
                    else -> {
                        GameResult.VICTORY
                    }
                }
            }

            Play.SCISSOR -> {
                when (botPlay) {
                    Play.ROCK -> {
                        GameResult.DEFEAT
                    }
                    Play.SCISSOR -> {
                        GameResult.TIE
                    }
                    else -> {
                        GameResult.VICTORY
                    }
                }
            }
        }

        resultState.value = result
        handleScore(result)
        handlePlays()
        return result
    }

    private fun handleScore(result: GameResult) {
        var botScore: Int = score.value?.first ?: 0 // javascript -> botScore ? score.value?.first : 0
        var playerScore: Int = score.value?.second ?: 0

        val sum = when (result) {
            GameResult.VICTORY -> Pair(+0, +10)
            GameResult.TIE -> Pair(+5, +5)
            GameResult.DEFEAT -> Pair(+10, +0)
        }

        botScore += sum.first // botScore = botScore + sum.first
        playerScore += sum.second

        score.value = Pair(botScore, playerScore)
    }

    private fun handlePlays() {
        if ((play.value ?: 0) == MAX_PLAYS) {
            resetGame()
        } else {
            play.value = play.value?.plus(1)
        }
    }

    private fun resetGame() {
        val botScore = score.value?.first ?: 0
        val playerScore = score.value?.second ?: 0

        val message = if (playerScore > botScore) {
            "You Won! c:"
        } else if (playerScore == botScore) {
            "Tie :p"
        } else {
            "You lost :c"
        }

        MaterialAlertDialogBuilder(this)
            .setMessage(message)
            .setPositiveButton("Ok", null)
            .setOnDismissListener {
                play.value = 1
                score.value = Pair(0, 0)
            }
            .show()
    }

    companion object {
        const val MAX_PLAYS = 10
    }
}
