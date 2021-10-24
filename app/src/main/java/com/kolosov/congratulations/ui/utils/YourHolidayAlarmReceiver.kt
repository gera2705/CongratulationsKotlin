package com.kolosov.congratulations.ui.utils

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kolosov.congratulations.MainActivity
import com.kolosov.congratulations.R

class YourHolidayAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context,MainActivity::class.java )
        intent!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0,i,0)

        val builder = NotificationCompat.Builder(context!!, "your_holiday")
            .setSmallIcon(R.drawable.ic_logo)
            .setColor(context.getColor(R.color.main_color))
            .setContentTitle("День рождения, Дядюшки!")
            .setContentText("Не забыть поздравить с юбилеем дядюшку!")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val manager = NotificationManagerCompat.from(context)
        manager.notify(123, builder.build())

    }
}
