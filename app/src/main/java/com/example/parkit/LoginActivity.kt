package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.loginButton.setOnClickListener {
            val phoneNumber = binding.NumeroEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (phoneNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginUser(phoneNumber, password)
        }

        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, PhoneNumberActivity::class.java))
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
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
