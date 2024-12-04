package com.example.parkit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BookingDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)

        val tvGetDirections = findViewById<TextView>(R.id.tvGetDirections)
        val btnBackToHome = findViewById<Button>(R.id.btnBackToHome)

        // Ação para obter direções
        tvGetDirections.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=ParkIT A"))
            startActivity(intent)
        }

        // Ação para voltar à página principal
        btnBackToHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
