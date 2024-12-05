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

        val editTextEmail: EditText = findViewById(R.id.NumeroEditText)
        val editTextPassword: EditText = findViewById(R.id.passwordEditText)
        val btnLogin: Button = findViewById(R.id.loginButton)
        val forgotPassword: TextView = findViewById(R.id.forgotPasswordTextView)

        // Ação do botão de login
        btnLogin.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login bem-sucedido
                    val user = auth.currentUser
                    Toast.makeText(this, "Login bem-sucedido", Toast.LENGTH_SHORT).show()

                    // Navegar para a próxima Activity (ex: PaymentActivity ou HomeActivity)
                    val intent = Intent(this, PaymentActivity::class.java)
                    startActivity(intent)
                    finish() // Finaliza a LoginActivity para que o usuário não volte para ela
                } else {
                    // Se o login falhar
                    Log.e("LoginActivity", "Erro ao fazer login", task.exception)
                    Toast.makeText(this, "Falha no login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
