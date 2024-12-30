package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

class VerificationCode : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var verificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.verification_code)

        auth = FirebaseAuth.getInstance()
        verificationId = intent.getStringExtra("verificationId")
        val phoneNumber = intent.getStringExtra("phoneNumber")

        val digit1 = findViewById<EditText>(R.id.verificationCodeDigit1)
        val digit2 = findViewById<EditText>(R.id.verificationCodeDigit2)
        val digit3 = findViewById<EditText>(R.id.verificationCodeDigit3)
        val digit4 = findViewById<EditText>(R.id.verificationCodeDigit4)
        val digit5 = findViewById<EditText>(R.id.verificationCodeDigit5)
        val digit6 = findViewById<EditText>(R.id.verificationCodeDigit6)

        setupAutoMove(digit1, digit2)
        setupAutoMove(digit2, digit3)
        setupAutoMove(digit3, digit4)
        setupAutoMove(digit4, digit5)
        setupAutoMove(digit5, digit6)

        findViewById<android.widget.Button>(R.id.verifyButton).setOnClickListener {
            val code = "${digit1.text}${digit2.text}${digit3.text}${digit4.text}${digit5.text}${digit6.text}".trim()
            if (code.length != 6) {
                Toast.makeText(this, "Por favor, insira o código completo de 6 dígitos", Toast.LENGTH_SHORT).show()
            } else {
                verifyCode(code, phoneNumber)
            }
        }
    }

    private fun verifyCode(code: String, phoneNumber: String?) {
        if (verificationId.isNullOrEmpty()) {
            Toast.makeText(this, "Erro ao obter o ID de verificação", Toast.LENGTH_SHORT).show()
            return
        }
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Código verificado com sucesso!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ChangePasswordActivity::class.java)
                intent.putExtra("phoneNumber", phoneNumber)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Erro ao verificar código", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupAutoMove(current: EditText, next: EditText?) {
        current.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) {
                    next?.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
