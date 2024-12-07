package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.databinding.ActivityVerificationCodeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

class VerificationCodeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerificationCodeBinding
    private lateinit var auth: FirebaseAuth
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configurar View Binding
        binding = ActivityVerificationCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Obter o ID de verificação da Intent
        verificationId = intent.getStringExtra("verificationId")

        // Configurar o botão "Verificar"
        binding.verifyButton.setOnClickListener {
            val code = binding.verificationCodeEditText.text.toString().trim()

            if (code.isNotEmpty() && verificationId != null) {
                verifyCode(code)
            } else {
                Toast.makeText(this, "Insira o código.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Usuário autenticado com sucesso!", Toast.LENGTH_LONG).show()

                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Erro ao verificar o código.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
