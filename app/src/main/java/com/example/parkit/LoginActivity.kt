package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.databinding.ActivityLoginBinding
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicialização do view binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicialização do Firestore
        db = FirebaseFirestore.getInstance()

        // Ação do botão "Login"
        binding.loginButton.setOnClickListener {
            val phoneNumber = binding.NumeroEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (phoneNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(phoneNumber, password)
        }

        // Ação do botão "Criar Conta"
        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, PhoneNumberActivity::class.java))
        }

        // Ação do texto "Esqueceu-se da palavra-passe?"
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)
        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(phoneNumber: String, password: String) {
        // Buscar as credenciais no Firestore
        db.collection("users").document(phoneNumber)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val savedPassword = document.getString("password")
                    if (savedPassword == password) {
                        Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show()
                        goToHomeScreen()
                    } else {
                        Toast.makeText(this, "Senha incorreta.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao fazer login: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun goToHomeScreen() {
        // Navega para a HomeActivity e limpa a pilha de atividades anteriores
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
