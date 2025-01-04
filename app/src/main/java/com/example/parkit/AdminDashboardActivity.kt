package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_view)

        val buttonUsage = findViewById<Button>(R.id.buttonUsage)
        val buttonStatistics = findViewById<Button>(R.id.buttonStatistics)
        val buttonImages = findViewById<Button>(R.id.buttonImages)
        val logoutButton = findViewById<ImageButton>(R.id.logoutButton)

        buttonUsage.setOnClickListener {
            val intent = Intent(this, ActivePermitActivity::class.java)
            startActivity(intent)
        }

        buttonStatistics.setOnClickListener {
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }

        buttonImages.setOnClickListener {
            Toast.makeText(this, "Funcionalidade em desenvolvimento!", Toast.LENGTH_SHORT).show()
        }

        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}