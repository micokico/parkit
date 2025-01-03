package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_screen3)

        val startButton: Button = findViewById(R.id.Start)
        startButton.setOnClickListener {
            Log.d("PaymentActivity", "Botão Start clicado")
            try {
                val intent = Intent(this@PaymentActivity, LoginActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Erro ao navegar: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
