package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class SpaceBookingActivity : AppCompatActivity() {

    private val firestore = FirebaseFirestore.getInstance()
    private var selectedSpot: String? = null
    private var currentFloor: Int = 1 // Valor padrão

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_booking)

        // Recebendo os dados passados pela ChooseSpaceActivity
        selectedSpot = intent.getStringExtra("SELECTED_SPOT")
        val parkingName = intent.getStringExtra("PARKING_NAME")
        val vehicleType = intent.getStringExtra("VEHICLE_TYPE")
        val parkingPrice = intent.getStringExtra("PARKING_PRICE")
        currentFloor = intent.getIntExtra("CURRENT_FLOOR", 1) // Adicionado para rastrear o andar

        // Atualizando os TextViews com os dados recebidos
        findViewById<TextView>(R.id.tvSelectedSpace).text = "Espaço: $selectedSpot"
        findViewById<TextView>(R.id.tvParkingName).text = "Nome do Estacionamento: $parkingName"
        findViewById<TextView>(R.id.tvParkingPrice).text = "Preço por hora: $parkingPrice"

        // Configurando o botão de reserva
        val bookButton = findViewById<Button>(R.id.btnBookSpace)
        bookButton.setOnClickListener {
            if (selectedSpot != null) {
                saveReservationToFirestore(selectedSpot!!, parkingName, vehicleType, parkingPrice)
            } else {
                Toast.makeText(this, "Erro ao recuperar espaço selecionado!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Salvar a reserva no Firestore
     */
    private fun saveReservationToFirestore(
        spotId: String,
        parkingName: String?,
        vehicleType: String?,
        parkingPrice: String?
    ) {
        val reservationData = mapOf(
            "spotId" to spotId,
            "parkingName" to parkingName,
            "vehicleType" to vehicleType,
            "parkingPrice" to parkingPrice,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection("reservations")
            .add(reservationData)
            .addOnSuccessListener {
                Toast.makeText(this, "Reserva concluída com sucesso!", Toast.LENGTH_SHORT).show()
                updateParkingSpotInFirestore(spotId)
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Erro ao salvar reserva: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Atualizar o estado do espaço de estacionamento no Firestore
     */
    private fun updateParkingSpotInFirestore(spotId: String) {
        val floorKey = "floor_$currentFloor"
        firestore.collection("parkingSpots").document(floorKey)
            .update(spotId, "car") // Define o estado do espaço como "ocupado"
            .addOnSuccessListener {
                Toast.makeText(this, "Espaço atualizado como reservado.", Toast.LENGTH_SHORT).show()
                finish() // Finaliza a atividade após a atualização
            }
            .addOnFailureListener { error ->
                Toast.makeText(this, "Erro ao atualizar o espaço: ${error.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
