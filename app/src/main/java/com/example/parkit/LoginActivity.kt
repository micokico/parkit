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


        auth = FirebaseAuth.getInstance()

        val editTextPhone: EditText = findViewById(R.id.NumeroEditText)
        val editTextPassword: EditText = findViewById(R.id.passwordEditText)
        val btnLogin: Button = findViewById(R.id.loginButton)
        val createAccount: TextView = findViewById(R.id.registerButton)


        btnLogin.setOnClickListener {
            val phoneNumber = editTextPhone.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (phoneNumber.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(phoneNumber, password)
            }
        }


        createAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(phoneNumber: String, password: String) {
        
        auth.signInWithEmailAndPassword(phoneNumber, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login bem-sucedido
                    Toast.makeText(this, "Login bem-sucedido", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeActivity::class.java)
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
