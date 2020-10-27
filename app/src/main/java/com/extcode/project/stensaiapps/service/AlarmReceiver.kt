package com.extcode.project.stensaiapps.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.other.*
import com.extcode.project.stensaiapps.screens.activity.SplashScreenActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(kExtraTitle)!!
        val message = intent.getStringExtra(kExtraMessage)!!
        val notificationId = intent.getIntExtra(kExtraID, 0)

        showAlarmNotification(context, title, message, notificationId)
    }

    fun setAlarm(
        context: Context,
        id: Int,
        title: String,
        message: String,
        date: String,
        time: String
    ) {
        if (isDateInvalid(date, kDateFormat) || isDateInvalid(time, kTimeFormat)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(kExtraID, id)
            putExtra(kExtraTitle, title)
            putExtra(kExtraMessage, message)
        }

        val arrDate = date.split("-").toTypedArray()
        val arrTime = time.split(":").toTypedArray()

        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, Integer.parseInt(arrDate[0]))
            set(Calendar.MONTH, Integer.parseInt(arrDate[1]) - 1)
            set(Calendar.DAY_OF_MONTH, Integer.parseInt(arrDate[2]))
            set(Calendar.HOUR_OF_DAY, Integer.parseInt(arrTime[0]))
            set(Calendar.MINUTE, Integer.parseInt(arrTime[1]))
            set(Calendar.SECOND, 0)
        }

        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis - 3600000,
            pendingIntent
        )

        Toast.makeText(context, "Deadline Set Up", Toast.LENGTH_SHORT).show()
    }

    private fun showAlarmNotification(
        context: Context,
        title: String,
        message: String,
        notificationId: Int
    ) {
        val channelId = "TaskId"
        val channelName = "TaskName"

        val notificationIntent = Intent(context, SplashScreenActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            context, notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_time)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(notificationSound)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(true)
                vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            }

            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notificationId, notification)
    }

    fun cancelAlarm(context: Context, id: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }

}
