package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RealPaymentActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val paymentsRef = database.getReference("payments")
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "default_user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val btnPay = findViewById<Button>(R.id.btnPay)

        btnPay.setOnClickListener {
            val cardNumber = findViewById<EditText>(R.id.etCardNumber).text.toString()
            val expiry = findViewById<EditText>(R.id.etExpiry).text.toString()
            val cvv = findViewById<EditText>(R.id.etCvv).text.toString()

            if (validateFields(cardNumber, expiry, cvv)) {
                savePaymentDetailsToFirebase(cardNumber, expiry, cvv)
            } else {
                Toast.makeText(this, "Preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateFields(cardNumber: String, expiry: String, cvv: String): Boolean {
        return cardNumber.length == 16 && expiry.contains("/") && cvv.length == 3
    }

    private fun savePaymentDetailsToFirebase(cardNumber: String, expiry: String, cvv: String) {
        val paymentId = paymentsRef.push().key // Gera um ID único
        val paymentData = mapOf(
            "userId" to userId,
            "cardNumber" to cardNumber.takeLast(4), // Só armazene os últimos 4 dígitos
            "expiry" to expiry,
            "cvv" to "###", // Nunca salve o CVV
            "status" to "Pendente"
        )

        paymentId?.let {
            paymentsRef.child(it).setValue(paymentData).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Pagamento salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    navigateToConfirmation()
                } else {
                    Toast.makeText(this, "Erro ao salvar pagamento!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToConfirmation() {
        val intent = Intent(this, SuccessActivity::class.java)
        startActivity(intent)
    }
}
