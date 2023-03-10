package com.example.notificationdemo

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi

class MainActivity : AppCompatActivity() {
    companion object {
        const val channelName = "test_channel_03"
        const val description = "test channel"
        const val importance = NotificationManager.IMPORTANCE_HIGH
        const val channelId = "com.example.notificationdemo"
    }

    val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createNotificationChannelIfNeed()
        setOnclickListener()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setOnclickListener() {
        findViewById<Button>(R.id.btn_normal_notification).apply {
            setOnClickListener {
                val notification = Notification.Builder(this@MainActivity, channelId)
                    .setContentTitle("normal notification title")
                    .setContentText("normal notification text")
                    .setSmallIcon(R.drawable.toutiao_icon)
                    .build()
                notificationManager.notify("tag", System.currentTimeMillis().toInt(), notification)
            }
        }

        findViewById<Button>(R.id.btn_set_channel_silent).apply {
            setOnClickListener {
                val channel = notificationManager.getNotificationChannel(channelId)
                channel.setSound(null, AudioAttributes.Builder().build())
            }
        }

        /**
         * 测试结果，无法通过再次创建同一个channelId的channel，来改变震动、声音，只能修改description和channelName
         */
        findViewById<Button>(R.id.btn_recreate_channel).apply {
            val newChannel = NotificationChannel(channelId, channelName + "_recreate", importance).apply {
                this.description = MainActivity.description + "_recreate"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(newChannel)
        }
    }

    private fun createNotificationChannelIfNeed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel(channelId)
            Log.d("Yangshili", "channel vibrate? " + channel?.shouldVibrate() + " channel des: " + channel?.description + " channel name: " + channel?.name)
            if (channel == null) {
                val newChannel = NotificationChannel(channelId, channelName, importance).apply {
                    this.description = MainActivity.description
                    enableVibration(true)
//                    enableLights(true)
                }
                notificationManager.createNotificationChannel(newChannel)
            }

        }
    }
}