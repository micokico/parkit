package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NavigationActivity : AppCompatActivity() {

    private lateinit var skipButton: TextView
    private lateinit var nextButton: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        skipButton = findViewById(R.id.skip_button)
        nextButton = findViewById(R.id.next_button)

        // Configuração dos eventos de clique
        skipButton.setOnClickListener {
            // Vai para a tela principal
            goToLastScreen()
        }

        nextButton.setOnClickListener {
            // Atualiza para a próxima tela
            goToNextScreen()
        }
    }

    private fun goToLastScreen() {
        // Finaliza o onboarding e vai para a tela principal
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun goToNextScreen() {
        // Muda para a próxima tela do onboarding
        // Substitua pela lógica necessária, como um ViewPager ou indicador de progresso
        startActivity(Intent(this, PaymentActivity::class.java))
    }
}
