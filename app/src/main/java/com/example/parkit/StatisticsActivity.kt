package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.R

class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_estatisticas)


        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, AdminDashboardActivity::class.java)
            startActivity(intent)
            finish()
        }


        val btnTotalUsers = findViewById<Button>(R.id.buttonUserCount)
        btnTotalUsers.setOnClickListener {
        }

        val btnGlobalRevenue = findViewById<Button>(R.id.buttonGlobalRevenue)
        btnGlobalRevenue.setOnClickListener {
            val intent = Intent(this, GlobalRevenueActivity::class.java)
            startActivity(intent)
        }

    }
}
