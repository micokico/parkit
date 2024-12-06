package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializando o FirebaseAuth
        auth = FirebaseAuth.getInstance()

        val editTextPhone: EditText = findViewById(R.id.NumeroEditText) // Número de telefone
        val editTextPassword: EditText = findViewById(R.id.passwordEditText) // Senha
        val btnLogin: Button = findViewById(R.id.loginButton) // Botão para fazer login
        val createAccount: TextView = findViewById(R.id.registerButton)


        // Ação do botão "Login"
        btnLogin.setOnClickListener {
            val phoneNumber = editTextPhone.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (phoneNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(phoneNumber, password)
            }
        }

        // Ação do texto "Criar Conta"
        createAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(phoneNumber: String, password: String) {
        // Implementação de login com telefone e senha
        auth.signInWithEmailAndPassword(phoneNumber, password) // Firebase Auth usa email, substituir conforme necessidade
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login bem-sucedido
                    Toast.makeText(this, "Login bem-sucedido", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, PaymentActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Falha no login
                    Log.e("LoginActivity", "Erro ao fazer login", task.exception)
                    Toast.makeText(this, "Falha no login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
