package com.example.parkit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.databinding.ActivityOnboardingScreenBinding

class OnBoardingActivity : AppCompatActivity() {

    // Declaração do ViewBinding
    private lateinit var binding: ActivityOnboardingScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o ViewBinding para vincular o layout
        binding = ActivityOnboardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuração dos cliques dos botões
        setupButtonListeners()
    }

    // Configuração dos listeners para os botões
    private fun setupButtonListeners() {
        // Clique no botão "SKIP"
        binding.skipButton.setOnClickListener {
            goToMainScreen()
        }

        // Clique no botão "SEGUINTE"
        binding.nextButton.setOnClickListener {
            goToNextOnboardingScreen()
        }
    }

    // Navega para a tela principal
    private fun goToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finaliza a atividade atual para evitar o retorno a ela
    }

    // Navega para a próxima tela de onboarding
    private fun goToNextOnboardingScreen() {
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
    }
}
