package com.codecool.intermittime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import io.reactivex.rxjava3.subjects.Subject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    companion object{
        private  const val START_TIME_IN_MILLIS : Long = 600000
        private const val RUNNING_TAG = "Timer running?"
    }

    private lateinit var countDownTimer: CountDownTimer

    //private var isTimerRunning : Boolean? = null
    private var runningObservable = Variable(false)

    private var timeLeftInMillis = START_TIME_IN_MILLIS


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        runningObservable.observable.subscribe{
            Log.d(RUNNING_TAG, "onCreate: ${runningObservable.value}")
        }

        startButton.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                if (runningObservable.value){
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
        updateButton()
        //
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        runningObservable.value = false
        updateButton()
        //
    }

    private fun startTimer() {
        countDownTimer = object: CountDownTimer(timeLeftInMillis, 1000){
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateCountDowndText()
            }

            override fun onFinish() {
                runningObservable.value = false
                updateButton()
            }
        }.start()

        runningObservable.value = true
        updateButton()
    }

    private fun updateCountDowndText() {
        val minutes : Int = (timeLeftInMillis.toInt() / 1000) / 60
        val seconds : Int = (timeLeftInMillis.toInt() / 1000) % 60

        val timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes,seconds)
        counter.text = timeLeftFormatted
    }

    private fun updateButton(){
        if (runningObservable.value){
            resetButton.visibility = View.INVISIBLE
            startButton.text = "pause"
        }else{
            startButton.text = "start"

            if (timeLeftInMillis < 1000){
                startButton.visibility = View.INVISIBLE
            } else {
                startButton.visibility = View.VISIBLE
            }

            if(timeLeftInMillis < START_TIME_IN_MILLIS){
                resetButton.visibility = View.VISIBLE
            } else {
                resetButton.visibility = View.INVISIBLE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("millisLeft", timeLeftInMillis)
        outState.getBoolean("timerRunning", runningObservable.value)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        timeLeftInMillis = savedInstanceState.getLong("millisLeft")
        runningObservable.value = savedInstanceState.getBoolean("timerRunning")
        updateCountDowndText()
        updateButton()

        if (runningObservable.value){
            startTimer()
        }
    }
}

class Variable<T>(private val defaultValue: T) {
    var value: T = defaultValue
        set(value) {
            field = value
            observable.onNext(value)
        }
    val observable = BehaviorSubject.createDefault(value)
}