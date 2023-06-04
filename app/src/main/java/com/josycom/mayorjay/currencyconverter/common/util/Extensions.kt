package com.josycom.mayorjay.currencyconverter.common.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.josycom.mayorjay.currencyconverter.R
import kotlin.math.roundToInt

fun Any?.isEmptyOrNull(): Boolean {
    if (this is Collection<*>?) return this.isNullOrEmpty()
    if (this is String?) return this.isNullOrEmpty()
    return this == null
}

fun String.showToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

fun Context.showNotification(content: String?) {
    val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelId = Constants.NOTIFICATION_CHANNEL_ID
    val channelName = Constants.NOTIFICATION_CHANNEL_NAME
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        manager.createNotificationChannel(channel)
    }
    val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
        .setContentTitle(Constants.NOTIFICATION_TITLE)
        .setContentText(content)
        .setSmallIcon(R.mipmap.ic_launcher)
    manager.notify(Constants.NOTIFICATION_ID, builder.build())
}

fun String.extractCurrencyCode(): String {
    return this.substring(this.lastIndexOf("(") + 1, this.lastIndexOf(")"))
}

fun TextView.setTextContent(rate: Double, amount: Double) {
    if (amount > 0.0) {
        this.text = this.context.getString(R.string.amount, String.format("%.2f", amount))
    } else if (rate > 0.0) {
        this.text = this.context.getString(R.string.rate, String.format("%.2f", rate))
    }
}

fun Float.dpToPx(): Int {
    return (this * (Resources.getSystem().displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
}