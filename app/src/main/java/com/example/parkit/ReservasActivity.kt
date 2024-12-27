package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.databinding.ActivityReservasBinding

class ReservasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReservasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vincula o layout ao Binding
        binding = ActivityReservasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuração do botão de menu
        binding.menuButton.setOnClickListener {
            Toast.makeText(this, "Menu clicado!", Toast.LENGTH_SHORT).show()
        }

        // Configuração do botão "Voltar ao ecrã principal"
        binding.btnBackToHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Encerra a atividade atual para evitar sobreposição
        }
    }
}
