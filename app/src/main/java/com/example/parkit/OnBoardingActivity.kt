package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OnBoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_screen)

        // Recupera os elementos da view usando findViewById
        val skipButton: TextView = findViewById(R.id.skip_button)
        val nextButton: TextView = findViewById(R.id.next_button)

        // Configuração dos listeners para os botões
        skipButton.setOnClickListener {
            goToMainScreen()
        }

        nextButton.setOnClickListener {
            goToNextOnboardingScreen()
        }
    }

    private fun goToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finaliza a tela atual
    }

    private fun goToNextOnboardingScreen() {
        val intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
    }
}
