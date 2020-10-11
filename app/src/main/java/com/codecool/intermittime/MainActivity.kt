package com.codecool.intermittime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object{
        const val START_TIME_IN_MILLIS : Long = 600000
    }

    private lateinit var countDownTimer: CountDownTimer
    private var isTimerRunning : Boolean = false
    private var timeLeftInMillis = START_TIME_IN_MILLIS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if (isTimerRunning){
                    pauseTimer()
                } else {
                    startTimer()
                }
            }
        })

        resetButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                resetTimer()
            }
        })

        updateCountDowndText()


    }

    private fun resetTimer() {
        timeLeftInMillis = START_TIME_IN_MILLIS
        updateCountDowndText()
        resetButton.visibility = View.INVISIBLE
        startButton.visibility = View.VISIBLE
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        isTimerRunning = false
        startButton.text = "start"
        resetButton.visibility = View.VISIBLE
    }

    private fun startTimer() {
        countDownTimer = object: CountDownTimer(timeLeftInMillis, 1000){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDowndText()
            }

            override fun onFinish() {
                isTimerRunning = false
                startButton.text = "start"
                startButton.visibility = View.INVISIBLE
                resetButton.visibility = View.VISIBLE

            }
        }.start()

        isTimerRunning = true
        startButton.text = "pause"
        resetButton.visibility = View.INVISIBLE
    }

    private fun updateCountDowndText() {
        var minutes : Int = (timeLeftInMillis.toInt() / 1000) / 60
        var seconds : Int = (timeLeftInMillis.toInt() / 1000) % 60

        var timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes,seconds)
        counter.text = timeLeftFormatted
    }
}