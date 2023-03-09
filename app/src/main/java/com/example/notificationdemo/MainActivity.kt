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
        const val channelName = "test_channel_01"
        const val description = "test channel"
        const val importance = NotificationManager.IMPORTANCE_HIGH
        const val channelId = "com.example.notificationdemo"
    }

    val notificationManager by lazy {getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager}

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
                    .setSmallIcon(R.drawable.notification_icon)
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

        findViewById<Button>(R.id.btn_set_channel_vibrate_and_sound).apply {
//            setOnClickListener {
//                MediaPlayer().apply {
//                    setDataSource(this@MainActivity, Uri.parse("https://downsc.chinaz.net/Files/DownLoad/sound1/202103/s1024.mp3"))
//                    setAudioStreamType(AudioManager.STREAM_MUSIC)
//                    prepareAsync()
//                    setOnPreparedListener { start() }
//                }
//            }
        }
    }

    private fun createNotificationChannelIfNeed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel(channelId)
            if (channel == null) {
                val newChannel = NotificationChannel(channelId, channelName, importance).apply {
                    this.description = MainActivity.description
                    val soundPath = "https://downsc.chinaz.net/Files/DownLoad/sound1/202103/s1024.mp3"
                    setSound(Uri.parse(soundPath), AudioAttributes.Builder().build())
                    enableVibration(true)
                    enableLights(true)
                }
                notificationManager.createNotificationChannel(newChannel)
            }

        }
    }
}