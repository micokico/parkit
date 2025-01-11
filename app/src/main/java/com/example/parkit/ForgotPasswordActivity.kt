package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.databinding.ActivityForgotPasswordBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var auth: FirebaseAuth
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnBack.setOnClickListener { onBackPressed() }

        binding.btnSendRecovery.setOnClickListener {
            val phoneNumber = binding.etPhoneNumber.text.toString().trim()
            if (phoneNumber.isEmpty()) {
                Toast.makeText(this, "Por favor, insira o número de telefone.", Toast.LENGTH_SHORT).show()
            } else {
                sendVerificationCode("+351$phoneNumber")
            }
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Toast.makeText(this@ForgotPasswordActivity, "Verificação automática concluída", Toast.LENGTH_SHORT).show()
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Toast.makeText(this@ForgotPasswordActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            this@ForgotPasswordActivity.verificationId = verificationId
            Toast.makeText(this@ForgotPasswordActivity, "Código enviado", Toast.LENGTH_SHORT).show()
            navigateToVerificationCode(phoneNumber = binding.etPhoneNumber.text.toString().trim(), verificationId = verificationId)
        }
    }

    private fun navigateToVerificationCode(phoneNumber: String, verificationId: String) {
        val intent = Intent(this, VerificationCode::class.java)
        intent.putExtra("phoneNumber", phoneNumber)
        intent.putExtra("verificationId", verificationId)
        startActivity(intent)
    }
}
