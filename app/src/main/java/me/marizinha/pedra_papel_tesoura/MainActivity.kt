package me.marizinha.pedra_papel_tesoura

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import me.marizinha.pedra_papel_tesoura.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val botPlayState = MutableLiveData<Play>()
    private val playerPlayState = MutableLiveData<Play>()
    private val resultState = MutableLiveData<GameResult>()
    private val score = MutableLiveData(Pair(0, 0)) // Pair<BotScore, PlayerScore>

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
}
