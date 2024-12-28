package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.databinding.ActivityReservasBinding

class ReservasActivity : AppCompatActivity() {

    // Declaração do Binding para acessar os elementos do layout
    private lateinit var binding: ActivityReservasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Vincula o layout ao Binding
        binding = ActivityReservasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuração do botão de retorno no cabeçalho
        binding.btnBack.setOnClickListener {
            // Mensagem ao clicar no botão "Voltar"
            Toast.makeText(this, "Voltando para a tela anterior!", Toast.LENGTH_SHORT).show()
            finish() // Encerra a atividade atual
        }

    }
}
