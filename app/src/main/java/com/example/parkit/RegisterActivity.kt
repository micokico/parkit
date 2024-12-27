package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val phoneNumber = intent.getStringExtra("phoneNumber") ?: ""

        Log.d("RegisterActivity", "Número de telefone recebido: $phoneNumber")

        binding.createAccountButton.setOnClickListener {
            Log.d("RegisterActivity", "Botão Criar Conta clicado")

            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("RegisterActivity", "Tentando salvar credenciais...")
            saveCredentials(phoneNumber, password)
        }
    }

    private fun saveCredentials(phoneNumber: String, password: String) {
        val userData = hashMapOf(
            "phoneNumber" to phoneNumber,
            "password" to password
        )

        db.collection("users").document(phoneNumber)
            .set(userData)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Conta criada com sucesso no Firestore")
                Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
                goToLoginScreen()
            }
            .addOnFailureListener { e ->
                Log.e("RegisterActivity", "Erro ao criar conta: ${e.message}")
                Toast.makeText(this, "Erro ao criar conta: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun goToLoginScreen() {
        Log.d("RegisterActivity", "Redirecionando para LoginActivity")
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
