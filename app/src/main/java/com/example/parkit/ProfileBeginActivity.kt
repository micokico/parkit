package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.LinearLayout // Adicione esta importação
import android.widget.Toast // Importação do Toast
import androidx.appcompat.app.AppCompatActivity
import android.app.AlertDialog
import android.widget.Button
import android.util.Log



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

        // Ação do botão "Perfil"
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
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

        // Ação para o botão da imagem do parque
        val parkingCard = findViewById<LinearLayout>(R.id.parking_card) // Para todo o cartão
        parkingCard.setOnClickListener {
            val intent = Intent(this, ChooseSpaceActivity::class.java)
            startActivity(intent)
        }


        // Teste do botão "parking_button"
        val parkingButton = findViewById<Button>(R.id.parking_button)
        parkingButton.setOnClickListener {
            // Adicionando um log para verificar se o clique foi registrado
            Log.d("ProfileBeginActivity", "Botão 'parking_button' clicado!")

            // Exibindo o Toast
            Toast.makeText(this, "Funcionou!", Toast.LENGTH_SHORT).show()
        }

    }
}
