package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class OnBoardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_screen)

        val skipButton: TextView = findViewById(R.id.skipButton)
        val nextButton: TextView = findViewById(R.id.nextButton)

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
