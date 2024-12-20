package com.example.parkit

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
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
            // Exibe um diálogo de confirmação
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Confirmar saída")
            builder.setMessage("Tem certeza de que deseja sair da aplicação?")

            builder.setPositiveButton("Sim") { _, _ ->
                // Lógica de logout
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Limpa a pilha de atividades
                startActivity(intent)
                finish() // Finaliza a atividade atual
            }

            builder.setNegativeButton("Não") { dialog, _ ->
                dialog.dismiss() // Fecha o diálogo se o usuário cancelar
            }

            // Exibe o diálogo
            val dialog = builder.create()
            dialog.show()
        }

        // Ação para o item "Perfil"
        profileButton.setOnClickListener {
            // Aqui você pode adicionar o código para carregar o perfil do usuário
        }

        // Ação para o item "Meus carros"
        myCarsButton.setOnClickListener {
            val intent = Intent(this, MyCarActivity::class.java)
            startActivity(intent)
        }

        // Ação para o item "Histórico"
        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }
}
