package com.example.parkit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val etPhoneNumber = findViewById<EditText>(R.id.etPhoneNumber)
        val btnSendRecovery = findViewById<Button>(R.id.btnSendRecovery)

        // Lógica para enviar código de recuperação
        btnSendRecovery.setOnClickListener {
            val phoneNumber = etPhoneNumber.text.toString()

            if (phoneNumber.isEmpty()) {
                Toast.makeText(this, "Por favor, insira o número de telefone.", Toast.LENGTH_SHORT).show()
            } else {
                // Aqui seria implementado o envio do código de recuperação (API ou SMS)
                Toast.makeText(this, "Código de recuperação enviado para $phoneNumber.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
