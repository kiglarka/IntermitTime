package com.codecool.intermittime

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.codecool.intermittime.App.Companion.CHANNEL_ID
import kotlin.properties.Delegates

enum class TimerType()

class TimerService : Service() {

    private var startTime by Delegates.notNull<Long>()

    fun getRunningTime() : Long{
        return System.currentTimeMillis() - startTime
    }

    override fun onCreate() {
        super.onCreate()
        startTime = System.currentTimeMillis()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val input = intent?.getStringExtra("input")
        val notificationIntent = Intent(this,MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0)

        val notification : Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Timer Service")
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_android)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1,notification)

        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}