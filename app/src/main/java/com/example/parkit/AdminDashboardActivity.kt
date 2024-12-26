package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.admin_view)

        // Encontrar os botões pelo ID
        val buttonUsage = findViewById<Button>(R.id.buttonUsage)
        val buttonStatistics = findViewById<Button>(R.id.buttonStatistics)
        val buttonImages = findViewById<Button>(R.id.buttonImages)

        // Configurar ações para os botões
        buttonUsage.setOnClickListener {
            Toast.makeText(this, "Funcionalidade em desenvolvimento!", Toast.LENGTH_SHORT).show()
        }

        buttonStatistics.setOnClickListener {
            // Redirecionar para a página de estatísticas
            val intent = Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }

        buttonImages.setOnClickListener {
            Toast.makeText(this, "Funcionalidade em desenvolvimento!", Toast.LENGTH_SHORT).show()
        }
    }
}
