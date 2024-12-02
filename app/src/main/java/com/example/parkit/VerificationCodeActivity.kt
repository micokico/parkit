package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerificationCodeActivity : AppCompatActivity() {

    private lateinit var codeEditText: EditText
    private lateinit var verifyButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var verificationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code)

        auth = FirebaseAuth.getInstance()

        // Obtém o ID de verificação passado pela intenção
        verificationId = intent.getStringExtra("verificationId").toString()

        codeEditText = findViewById(R.id.codeEditText)
        verifyButton = findViewById(R.id.verifyButton)

        verifyButton.setOnClickListener {
            val code = codeEditText.text.toString().trim()
            if (code.isNotEmpty()) {
                verifyCode(code)
            } else {
                Toast.makeText(this, "Por favor, insira o código de verificação.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credential)
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Redirecionar para a página de criar senha
                    val intent = Intent(this, SetPasswordActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Falha ao verificar o código: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
