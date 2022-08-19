package com.bogdanmurzin.mypersonalwallet.services

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.bogdanmurzin.domain.entities.Transaction
import com.bogdanmurzin.domain.usecases.transaction.GetTransactionsUseCase
import com.bogdanmurzin.mypersonalwallet.BuildConfig
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.common.Constants.FLAGS
import com.bogdanmurzin.mypersonalwallet.common.Constants.NOTIFICATION_SAVE_ID
import com.bogdanmurzin.mypersonalwallet.common.Constants.NOTIFICATION_SERVICE_CHANNEL
import com.bogdanmurzin.mypersonalwallet.common.Constants.NOTIFICATION_SERVICE_CHANNEL_NAME
import com.bogdanmurzin.mypersonalwallet.common.Constants.REQUEST_CODE
import com.bogdanmurzin.mypersonalwallet.ui.activity.MainActivity
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import javax.inject.Inject

@AndroidEntryPoint
class SaveToFileForegroundService : Service() {

    @Inject
    lateinit var getTransactionsUseCase: GetTransactionsUseCase

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action != null && intent.action.equals(ACTION_STOP_FOREGROUND, true)) {
            stopForegroundService()
        }
        saveTransactions()
        generateForegroundNotification()
        return START_STICKY
    }

    fun stopForegroundService() {
        stopForeground(true)
        stopSelf()
    }

    @SuppressLint("WrongConstant", "UnspecifiedImmutableFlag")
    private fun generateForegroundNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intentMainLanding = Intent(this, MainActivity::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, REQUEST_CODE, intentMainLanding, FLAGS)
            val iconNotification = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            val titleNotification = this.getString(R.string.notification_save_title)
            val subtitleNotification = this.getString(R.string.notification_save_subtitle)
            val mNotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel =
                    NotificationChannel(
                        NOTIFICATION_SERVICE_CHANNEL, NOTIFICATION_SERVICE_CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_MIN
                    )
                notificationChannel.enableLights(false)
                notificationChannel.lockscreenVisibility = Notification.VISIBILITY_SECRET
                mNotificationManager.createNotificationChannel(notificationChannel)
            }
            val builder = NotificationCompat.Builder(this, NOTIFICATION_SERVICE_CHANNEL)

            builder
                .setContentTitle(titleNotification).setContentText(subtitleNotification)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setWhen(0)
                .setProgress(0, 0, true)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setOngoing(true)
            if (iconNotification != null) {
                builder.setLargeIcon(Bitmap.createScaledBitmap(iconNotification, 128, 128, false))
            }
            val notification = builder.build()
            startForeground(NOTIFICATION_SAVE_ID, notification)
        }
    }

    private fun saveTransactions() {
        CoroutineScope(Dispatchers.IO).launch {
            getTransactionsUseCase.invoke().collect {
                saveToJson(it)
            }
        }
    }

    private suspend fun saveToJson(list: List<Transaction>) {
        FileWriter(DIRECTORY_TO_FILE).use { writer ->
            val gson = GsonBuilder().create()
            gson.toJson(list, writer)
        }
        // Artificial delay 3 sec
        delay(DELAY_TIME)
        stopForegroundService()
    }

    companion object {
        const val ACTION_STOP_FOREGROUND = "${BuildConfig.APPLICATION_ID}.stopforeground"

        private const val FILE_NAME = "transactions.json"
        val DIRECTORY_TO_FILE: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + '/' + FILE_NAME)
        const val DELAY_TIME = 3_000L

    }

}