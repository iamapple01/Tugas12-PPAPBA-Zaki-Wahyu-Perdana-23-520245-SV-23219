package com.example.tugas12

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.tugas12.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val channelId = "TEST_NOTIF"
    private val notifId = 90

    private var likeCount = 0
    private var dislikeCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        registerReceiver(notificationReceiver, IntentFilter("ACTION_LIKE"))
        registerReceiver(notificationReceiver, IntentFilter("ACTION_DISLIKE"))

        val notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Test Notification", NotificationManager.IMPORTANCE_DEFAULT)
            notifManager.createNotificationChannel(channel)
        }

        binding.btnUpdate.setOnClickListener {
            val notifImage = BitmapFactory.decodeResource(resources, R.drawable.rikka)


            val likeIntent = Intent("ACTION_LIKE")
            val likePendingIntent = PendingIntent.getBroadcast(this, 0, likeIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


            val dislikeIntent = Intent("ACTION_DISLIKE")
            val dislikePendingIntent = PendingIntent.getBroadcast(this, 0, dislikeIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_bell)
                .setContentTitle("リッカはとてもかわいいですよね？")
                .setContentText("Translated : Rikka is so cute, right?")
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(notifImage))
                .addAction(R.drawable.like, "Like", likePendingIntent)
                .addAction(R.drawable.dislike, "Dislike", dislikePendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            notifManager.notify(notifId, builder.build())
        }
    }

    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                "ACTION_LIKE" -> {
                    likeCount++
                    binding.txtLikeCount.text = "Likes: $likeCount"
                }
                "ACTION_DISLIKE" -> {
                    dislikeCount++
                    binding.txtDislikeCount.text = "Dislikes: $dislikeCount"
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notificationReceiver)
    }
}
