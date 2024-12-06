package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializando o FirebaseAuth
        auth = FirebaseAuth.getInstance()

        val editTextEmail: EditText = findViewById(R.id.NumeroEditText) // Utilizado para número de telefone
        val editTextPassword: EditText = findViewById(R.id.passwordEditText) // Não utilizado para login com telefone
        val btnLogin: Button = findViewById(R.id.loginButton) // Será o botão para enviar o código
        val forgotPassword: TextView = findViewById(R.id.forgotPasswordTextView) // Pode ser usado para reenviar o código

        // Ação para enviar o código de verificação
        btnLogin.setOnClickListener {
            val phoneNumber = editTextEmail.text.toString()

            if (phoneNumber.isEmpty()) {
                Toast.makeText(this, "Por favor, insira o número de telefone", Toast.LENGTH_SHORT).show()
            } else {
                sendVerificationCode(phoneNumber)
            }
        }

        forgotPassword.setOnClickListener {
            val code = editTextPassword.text.toString()

            if (code.isEmpty()) {
                Toast.makeText(this, "Por favor, insira o código de verificação", Toast.LENGTH_SHORT).show()
            } else {
                verifyCode(code)
            }
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {

                    signInWithCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.e("LoginActivity", "Falha na verificação", e)
                    Toast.makeText(this@LoginActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verificationId, token)
                    this@LoginActivity.verificationId = verificationId
                    Toast.makeText(this@LoginActivity, "Código enviado", Toast.LENGTH_SHORT).show()
                }
            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyCode(code: String) {
        val credential = verificationId?.let {
            PhoneAuthProvider.getCredential(it, code)
        }

        if (credential != null) {
            signInWithCredential(credential)
        } else {
            Toast.makeText(this, "Erro na verificação do código", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login bem-sucedido
                    Toast.makeText(this, "Login bem-sucedido", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, PaymentActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Login falhou
                    Log.e("LoginActivity", "Erro ao fazer login", task.exception)
                    Toast.makeText(this, "Falha no login: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
