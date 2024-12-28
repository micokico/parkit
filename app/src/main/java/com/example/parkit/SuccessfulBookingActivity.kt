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
}
