package com.example.notificationhistory

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListenerService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val notification = sbn.notification
        val extras = notification?.extras

        // Check if the notification contains a message related to transactions
        if (extras != null) {
            val message = extras.getCharSequence("android.text")?.toString()
            if (message != null && (message.contains("credited") || message.contains("debited"))) {
                Log.d("NotificationListener", "Transaction Message: $message")
                if (message != null) {
                    // Store in SharedPreferences
                    val sharedPreferences = getSharedPreferences("TransactionMessages", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString(System.currentTimeMillis().toString(), message) // Using timestamp as key
                    editor.apply()
                }
            }
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        // Handle notification removal if necessary
    }
}