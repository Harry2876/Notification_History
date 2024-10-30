package com.example.notificationhistory

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("TransactionMessages", MODE_PRIVATE)

        // Check for notification listener permission
        if (!isNotificationServiceEnabled()) {
            showNotificationAccessDialog()
        } else {
            // If permission is already granted, load transactions
            loadTransactions()
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val packageName = packageName
        val enabledListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return enabledListeners?.contains(packageName) == true
    }

    private fun showNotificationAccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Notification Access Required")
        builder.setMessage("This app needs permission to read your notifications for tracking transactions. Please enable notification access in the settings.")
        builder.setPositiveButton("Go to Settings") { _, _ ->
            // Open notification settings
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        builder.setCancelable(false)
        builder.show()
    }

    private fun loadTransactions() {
        // Load stored transaction messages
        val transactions = mutableListOf<String>()

        sharedPreferences.all.forEach { entry ->
            transactions.add(entry.value as String)
        }

        // Set up RecyclerView with transactions
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        transactionAdapter = TransactionAdapter(transactions)
        recyclerView.adapter = transactionAdapter
    }
}