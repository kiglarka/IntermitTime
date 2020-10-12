package com.codecool.intermittime

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App : Application() {

    companion object{
        const val CHANNEL_ID = "timerServiceChannel"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val timerChannel : NotificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Timer service channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager : NotificationManager? = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(timerChannel)
        }
    }
}