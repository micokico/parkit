package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.databinding.ActivityChangePasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val phoneNumber = intent.getStringExtra("phoneNumber") ?: ""

        if (phoneNumber.isEmpty()) {
            Log.e("ChangePasswordActivity", "Número de telefone não fornecido")
            Toast.makeText(this, "Erro: Número de telefone ausente", Toast.LENGTH_SHORT).show()
            finish() // Fecha a atividade para evitar estado inválido
            return
        }

        Log.d("ChangePasswordActivity", "Número de telefone recebido: $phoneNumber")

        binding.changePasswordButton.setOnClickListener {
            val newPassword = binding.newPasswordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("ChangePasswordActivity", "Tentando atualizar senha...")
            updatePassword(phoneNumber, newPassword)
        }
    }

    private fun updatePassword(phoneNumber: String, newPassword: String) {
        db.collection("users").document(phoneNumber)
            .update("password", newPassword) // Atualiza apenas o campo "password"
            .addOnSuccessListener {
                Log.d("ChangePasswordActivity", "Senha atualizada com sucesso no Firestore")
                Toast.makeText(this, "Senha atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                goToLoginScreen()
            }
            .addOnFailureListener { e ->
                Log.e("ChangePasswordActivity", "Erro ao atualizar senha: ${e.message}")
                Toast.makeText(this, "Erro ao atualizar senha: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun goToLoginScreen() {
        Log.d("ChangePasswordActivity", "Redirecionando para LoginActivity")
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
