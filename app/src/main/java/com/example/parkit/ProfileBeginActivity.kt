package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileBeginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        // Botões do menu
        val profileButton = findViewById<TextView>(R.id.profileTextView)
        val myCarsButton = findViewById<TextView>(R.id.myCarsTextView)
        val historyButton = findViewById<TextView>(R.id.historyTextView)

        // Ação do botão de "Voltar"
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressed()  // Volta para a tela anterior
        }

        // Ação do botão de "Sair"
        val logoutButton = findViewById<ImageButton>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            // Aqui você pode adicionar a lógica para sair, como limpar as preferências de login
            // E depois redirecionar para a tela de login ou a inicial
        }

        // Ação para o item "Perfil"
        profileButton.setOnClickListener {
            // Aqui você pode adicionar o código para carregar o perfil do usuário
        }

        myCarsButton.setOnClickListener {
            // Navega para a tela "Meus carros"
            val intent = Intent(this, MyCarActivity::class.java)
            startActivity(intent)
        }

        // Ação para o item "Histórico"
        historyButton.setOnClickListener {
            // Navega para a tela "Histórico"
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }
}
