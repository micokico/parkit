package com.example.parkit

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val notificationsLayout = findViewById<LinearLayout>(R.id.notificationsLayout)
        val sharedPreferences = getSharedPreferences("notifications", MODE_PRIVATE)
        val notifications = sharedPreferences.getString("notifications_list", "[]")
        val jsonArray = JSONArray(notifications)

        // Adicionar notificações dinamicamente
        for (i in 0 until jsonArray.length()) {
            val notification = jsonArray.getJSONObject(i)
            val title = notification.getString("title")
            val message = notification.getString("message")
            val timestamp = notification.getLong("timestamp")

            // Inflar o layout para cada notificação
            val notificationView = LayoutInflater.from(this).inflate(R.layout.notification_item, notificationsLayout, false)

            // Configurar os dados no layout
            notificationView.findViewById<TextView>(R.id.tvNotificationTitle).text = title
            notificationView.findViewById<TextView>(R.id.tvNotificationMessage).text = message
            notificationView.findViewById<TextView>(R.id.tvNotificationTime).text = formatTimestamp(timestamp)

            // Adicionar a visualização ao layout principal
            notificationsLayout.addView(notificationView)
        }

        // Configuração do botão "Voltar"
        val backButton = findViewById<ImageButton>(R.id.btn_back)
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("HH:mm dd/MM/yyyy", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }
}
