package com.example.parkit

import android.content.Intent  // Importação da classe Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.R

class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_estatisticas)


        val btnBack = findViewById<Button>(R.id.btnBack)
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
        }
    }
}
