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

        // Configura o botão de voltar
        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            // Cria uma Intent para voltar à tela AdminDashboardActivity
            val intent = Intent(this, AdminDashboardActivity::class.java)
            startActivity(intent)  // Inicia a AdminDashboardActivity
            finish()  // Fecha a StatisticsActivity
        }

        // Configura botões
        val btnTotalUsers = findViewById<Button>(R.id.buttonUserCount)
        btnTotalUsers.setOnClickListener {
            // Ação para exibir quantidade de utilizadores
        }

        val btnGlobalRevenue = findViewById<Button>(R.id.buttonGlobalRevenue)
        btnGlobalRevenue.setOnClickListener {
            // Ação para exibir receitas globais
        }
    }
}
