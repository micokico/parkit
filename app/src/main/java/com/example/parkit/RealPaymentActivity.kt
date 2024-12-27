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

    private val database = FirebaseDatabase.getInstance()  // Referência ao Firebase Realtime Database
    private val paymentsRef = database.getReference("payments")  // Referência à coleção de pagamentos
    private val firestore = FirebaseFirestore.getInstance()  // Referência ao Firebase Firestore
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "default_user"  // ID do usuário logado

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
            Log.d("Button", "Botão de pagamento clicado")
            val cardNumber = findViewById<EditText>(R.id.etCardNumber).text.toString().trim()
            val expiry = findViewById<EditText>(R.id.etExpiry).text.toString().trim()
            val cvv = findViewById<EditText>(R.id.etCvv).text.toString().trim()

            // Valida os campos antes de prosseguir
            if (validateFields(cardNumber, expiry, cvv)) {
                Log.d("Validation", "Campos validados com sucesso")
                savePaymentDetailsToFirebase(cardNumber, expiry, cvv, reservationId, totalCost)
            } else {
                Log.d("Validation", "Falha na validação dos campos")
                Toast.makeText(this, "Preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Função que valida os campos do cartão
    private fun validateFields(cardNumber: String, expiry: String, cvv: String): Boolean {
        val isCardValid = cardNumber.length == 16 && cardNumber.all { it.isDigit() }
        val isExpiryValid = expiry.matches(Regex("\\d{2}/\\d{2}"))  // Formato MM/AA
        val isCvvValid = cvv.length == 3 && cvv.all { it.isDigit() }

        Log.d("Validation", "Card: $isCardValid, Expiry: $isExpiryValid, CVV: $isCvvValid")
        return isCardValid && isExpiryValid && isCvvValid
    }

    // Função que salva os dados do pagamento no Firebase
    private fun savePaymentDetailsToFirebase(
        cardNumber: String,
        expiry: String,
        cvv: String,
        reservationId: String,
        totalCost: Double
    ) {
        val paymentId = paymentsRef.push().key  // Cria uma chave única para o pagamento

        if (paymentId == null) {
            Log.e("Payment", "Falha ao gerar ID de pagamento.")
            Toast.makeText(this, "Erro interno. Tente novamente.", Toast.LENGTH_SHORT).show()
            return
        }

        // Cria um mapa com os dados do pagamento
        val paymentData = mapOf(
            "userId" to userId,
            "reservationId" to reservationId,
            "cardNumber" to cardNumber.takeLast(4),  // Salva apenas os últimos 4 dígitos do número do cartão
            "expiry" to expiry,
            "cvv" to "###",  // Não salva o CVV por questões de segurança
            "totalCost" to totalCost,
            "status" to "Concluído"
        )

        // Salva os dados no Firebase Realtime Database
        paymentsRef.child(paymentId).setValue(paymentData).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Payment", "Dados de pagamento salvos com sucesso!")
                updateReservationStatus(reservationId)  // Atualiza o status da reserva
            } else {
                Log.e("Payment", "Erro ao salvar pagamento: ${task.exception?.message}")
                Toast.makeText(this, "Erro ao salvar pagamento!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Função que atualiza o status da reserva para "Pago" no Firestore
    private fun updateReservationStatus(reservationId: String) {
        firestore.collection("reservations").document(reservationId)
            .update("status", "Pago")
            .addOnSuccessListener {
                Log.d("Reservation", "Status atualizado para 'Pago'.")
                navigateToHomeActivity()  // Chama a navegação para HomeActivity
            }
            .addOnFailureListener { error ->
                Log.e("Reservation", "Erro ao atualizar reserva: ${error.message}")
                Toast.makeText(this, "Erro ao atualizar reserva!", Toast.LENGTH_SHORT).show()
            }
    }

    // Função que navega para a HomeActivity após o pagamento
    private fun navigateToHomeActivity() {
        // Exibe a mensagem de sucesso
        Toast.makeText(this, "Pagamento realizado com sucesso!", Toast.LENGTH_SHORT).show()

        // Redireciona para a HomeActivity
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()  // Finaliza a atividade atual para evitar voltar para ela
    }
}
