package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sucess) // Certifique-se de que o nome do layout é correto

        // Configura o botão para retornar à tela inicial
        val btnReturnHome = findViewById<Button>(R.id.btnReturnHome)
        btnReturnHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Finaliza esta atividade para evitar o retorno com o botão "voltar"
        }
    }
}
