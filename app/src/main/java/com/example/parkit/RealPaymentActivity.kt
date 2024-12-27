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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class RealPaymentActivity : AppCompatActivity() {

    private val database = FirebaseDatabase.getInstance()
    private val paymentsRef = database.getReference("payments")
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "default_user"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        // Recupera os dados enviados pela Intent
        val totalCost = intent.getDoubleExtra("TOTAL_COST", 0.0)
        val reservationId = intent.getStringExtra("RESERVATION_ID") ?: ""
        val selectedSpot = intent.getStringExtra("SELECTED_SPOT") ?: "Não informado"

        // Atualiza o TextView com o total a pagar
        val tvTotalFee = findViewById<TextView>(R.id.tvTotalFee)
        tvTotalFee.text = "Total a pagar: %.2f €".format(totalCost)

        // Atualiza o TextView com o lugar selecionado
        val tvParkingDetails = findViewById<TextView>(R.id.tvParkingDetails)
        tvParkingDetails.text = "Lugar Selecionado: $selectedSpot"

        // Configura o botão de pagamento
        val btnPay = findViewById<Button>(R.id.btnPay)
        btnPay.setOnClickListener {
            val cardNumber = findViewById<EditText>(R.id.etCardNumber).text.toString()
            val expiry = findViewById<EditText>(R.id.etExpiry).text.toString()
            val cvv = findViewById<EditText>(R.id.etCvv).text.toString()

            // Valida os campos antes de prosseguir
            if (validateFields(cardNumber, expiry, cvv)) {
                savePaymentDetailsToFirebase(cardNumber, expiry, cvv, reservationId, totalCost)
            } else {
                Toast.makeText(this, "Preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateFields(cardNumber: String, expiry: String, cvv: String): Boolean {
        val isCardValid = cardNumber.length == 16 && cardNumber.all { it.isDigit() }
        val isExpiryValid = expiry.matches(Regex("\\d{2}/\\d{2}"))
        val isCvvValid = cvv.length == 3 && cvv.all { it.isDigit() }

        Log.d("Validation", "Card: $isCardValid, Expiry: $isExpiryValid, CVV: $isCvvValid")
        return isCardValid && isExpiryValid && isCvvValid
    }

    private fun savePaymentDetailsToFirebase(
        cardNumber: String,
        expiry: String,
        cvv: String,
        reservationId: String,
        totalCost: Double
    ) {
        val paymentId = paymentsRef.push().key

        if (paymentId == null) {
            Log.e("Payment", "Falha ao gerar ID de pagamento.")
            Toast.makeText(this, "Erro interno. Tente novamente.", Toast.LENGTH_SHORT).show()
            return
        }

        val paymentData = mapOf(
            "userId" to userId,
            "reservationId" to reservationId,
            "cardNumber" to cardNumber.takeLast(4), // Salva apenas os últimos 4 dígitos
            "expiry" to expiry,
            "cvv" to "###", // O CVV não é salvo por questões de segurança
            "totalCost" to totalCost,
            "status" to "Concluído"
        )

        paymentsRef.child(paymentId).setValue(paymentData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Payment", "Dados de pagamento salvos com sucesso!")
                updateReservationStatus(reservationId)
            } else {
                Log.e("Payment", "Erro ao salvar pagamento: ${task.exception?.message}")
                Toast.makeText(this, "Erro ao salvar pagamento!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateReservationStatus(reservationId: String) {
        firestore.collection("reservations").document(reservationId)
            .update("status", "Pago")
            .addOnSuccessListener {
                Log.d("Reservation", "Status atualizado para 'Pago'.")
                navigateToConfirmation(reservationId)
            }
            .addOnFailureListener { error ->
                Log.e("Reservation", "Erro ao atualizar reserva: ${error.message}")
                Toast.makeText(this, "Erro ao atualizar reserva!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToConfirmation(reservationId: String) {
        Log.d("Navigation", "Navegando para SuccessActivity com ID da reserva: $reservationId")
        val intent = Intent(this, SuccessActivity::class.java)
        intent.putExtra("RESERVATION_ID", reservationId)
        startActivity(intent)
        finish() // Finaliza a atividade para evitar voltar
    }
}
