package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONObject

class SuccessfulBookingActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successful_booking)

        sharedPreferences = getSharedPreferences("notifications", MODE_PRIVATE)

        val tvSuccessMessage = findViewById<TextView>(R.id.tvSuccessMessage)
        val tvAdditionalMessage = findViewById<TextView>(R.id.tvAdditionalMessage)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val btnViewBookingDetails = findViewById<Button>(R.id.btnViewBookingDetails)

        // Adicionar notificação
        addNotification("Reserva Bem-Sucedida", "Parabéns! Sua reserva foi confirmada.")

        btnViewBookingDetails.setOnClickListener {
            Toast.makeText(this, "A abrir detalhes da reserva...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        Handler().postDelayed({
            tvAdditionalMessage.text = "Os Guardas foram avisados"
            progressBar.visibility = ProgressBar.INVISIBLE
        }, 3000)
    }

    private fun addNotification(title: String, message: String) {
        val notifications = sharedPreferences.getString("notifications_list", "[]")
        val jsonArray = JSONArray(notifications)
        val notification = JSONObject().apply {
            put("title", title)
            put("message", message)
            put("timestamp", System.currentTimeMillis())
        }
        jsonArray.put(notification)

        sharedPreferences.edit().putString("notifications_list", jsonArray.toString()).apply()
    }
}
