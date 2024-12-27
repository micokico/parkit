package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class RealPaymentActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val paymentsRef = database.getReference("payments")
    private val firestore = FirebaseFirestore.getInstance() // Inicializando o Firestore
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
        return cardNumber.length == 16 && expiry.matches(Regex("\\d{2}/\\d{2}")) && cvv.length == 3
    }

    private fun savePaymentDetailsToFirebase(cardNumber: String, expiry: String, cvv: String) {
        val paymentId = paymentsRef.push().key
        val totalCost = intent.getDoubleExtra("TOTAL_COST", 0.0)
        val reservationId = intent.getStringExtra("RESERVATION_ID") ?: ""

        val paymentData = mapOf(
            "userId" to userId,
            "reservationId" to reservationId,
            "cardNumber" to cardNumber.takeLast(4),
            "expiry" to expiry,
            "cvv" to "###",
            "totalCost" to totalCost,
            "status" to "Concluído"
        )

        paymentId?.let {
            paymentsRef.child(it).setValue(paymentData).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateReservationStatus(reservationId)
                } else {
                    Toast.makeText(this, "Erro ao salvar pagamento!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateReservationStatus(reservationId: String) {
        firestore.collection("reservations").document(reservationId)
            .update("status", "Pago")
            .addOnSuccessListener {
                Toast.makeText(this, "Pagamento concluído!", Toast.LENGTH_SHORT).show()
                navigateToConfirmation(reservationId)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao atualizar reserva!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToConfirmation(reservationId: String) {
        val intent = Intent(this, SuccessActivity::class.java)
        intent.putExtra("RESERVATION_ID", reservationId)
        startActivity(intent)
        finish() // Finaliza a atividade para evitar voltar
    }
}
