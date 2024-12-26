package com.example.parkit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.databinding.ActivityStatisticsBinding

class StatisticsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatisticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configuração do View Binding
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura o botão de voltar
        binding.btnBack.setOnClickListener {
            finish() // Fecha a atividade atual
        }

        // Configura botões
        binding.btnTotalUsers.setOnClickListener {
            // Ação para exibir quantidade de utilizadores
        }

        binding.btnGlobalRevenue.setOnClickListener {
            // Ação para exibir receitas globais
        }
    }
}
