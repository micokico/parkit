package com.example.parkit

import android.os.Bundle
import android.widget.Button  // Importação da classe Button
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.R

class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_estatisticas)

        // Configura o botão de voltar
        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        // Configura botões
        val btnTotalUsers = findViewById<Button>(R.id.buttonUserCount)
        btnTotalUsers.setOnClickListener {

        }

        val btnGlobalRevenue = findViewById<Button>(R.id.buttonGlobalRevenue)
        btnGlobalRevenue.setOnClickListener {

        }
    }
}
