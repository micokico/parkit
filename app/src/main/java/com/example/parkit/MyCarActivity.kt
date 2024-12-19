package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyCarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mycar)

        // Configuração do BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_settings -> {
                    // Ação para o Perfil
                    val intent = Intent(this, ProfileBeginActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Configuração do menu (ícone do menu)
        val menuIcon = findViewById<ImageView>(R.id.menuIcon)
        menuIcon.setOnClickListener {
            // Ação para abrir o menu ou fazer qualquer outra coisa
            Toast.makeText(this, "Menu Clicked", Toast.LENGTH_SHORT).show()
        }

        // Botão de Adicionar Carro
        val addCarButton = findViewById<Button>(R.id.addCarButton)
        addCarButton.setOnClickListener {
            // Adicione a lógica para adicionar um novo carro
            Toast.makeText(this, "Adicionar Carro", Toast.LENGTH_SHORT).show()
        }

        // Botão de Remover Carro
        val removeCarButton = findViewById<Button>(R.id.removeCarButton)
        removeCarButton.setOnClickListener {
            // Adicione a lógica para remover um carro
            Toast.makeText(this, "Remover Carro", Toast.LENGTH_SHORT).show()
        }

        // Se desejar configurar o título (ou qualquer outro comportamento relacionado ao título)
        val titleTextView = findViewById<TextView>(R.id.title)
        titleTextView.text = "Meus Carros"  // Definir o título da tela
    }
}
