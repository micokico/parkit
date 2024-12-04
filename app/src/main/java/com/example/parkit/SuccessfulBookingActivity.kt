package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SuccessfulBookingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_successful_booking)

        val tvSuccessMessage = findViewById<TextView>(R.id.tvSuccessMessage)
        val tvAdditionalMessage = findViewById<TextView>(R.id.tvAdditionalMessage)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val btnViewBookingDetails = findViewById<Button>(R.id.btnViewBookingDetails)

        // Configura a ação do botão "View Booking Details"
        btnViewBookingDetails.setOnClickListener {
            // Simulação de abrir uma nova página com detalhes da reserva
            Toast.makeText(this, "Opening booking details...", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, BookingDetailsActivity::class.java) // Substitua pela sua activity de detalhes
            startActivity(intent)
        }

        // Simula a conclusão de notificações com um atraso
        Handler().postDelayed({
            tvAdditionalMessage.text = "Security Guards Notified"
            progressBar.visibility = ProgressBar.INVISIBLE
        }, 3000) // 3 segundos de atraso
    }
}
