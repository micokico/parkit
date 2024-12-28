package com.example.parkit

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BookingDetailsActivity : AppCompatActivity() {

    private lateinit var parkingNameText: TextView
    private lateinit var dateText: TextView
    private lateinit var spotIdText: TextView
    private lateinit var totalCostText: TextView
    private lateinit var vehicleText: TextView
    private lateinit var vehicleTypeText: TextView
    private lateinit var backButton: ImageButton // Declarar o botão de voltar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)

        // Inicializar os componentes
        parkingNameText = findViewById(R.id.parkingNameText)
        dateText = findViewById(R.id.dateText)
        spotIdText = findViewById(R.id.spotIdText)
        totalCostText = findViewById(R.id.totalCostText)
        vehicleText = findViewById(R.id.vehicleText)
        vehicleTypeText = findViewById(R.id.vehicleTypeText)
        backButton = findViewById(R.id.btn_back) // Inicializar o botão de voltar

        // Configurar o comportamento do botão de voltar
        backButton.setOnClickListener {
            onBackPressed() // Chama o método para voltar à tela anterior
        }

        // Recuperar os dados da Intent
        val parkingName = intent.getStringExtra("PARKING_NAME") ?: "Desconhecido"
        val date = intent.getStringExtra("DATE") ?: "Desconhecida"
        val spotId = intent.getStringExtra("SPOT_ID") ?: "Desconhecido"
        val totalCost = intent.getIntExtra("TOTAL_COST", 0)
        val vehicle = intent.getStringExtra("VEHICLE") ?: "Desconhecido"
        val vehicleType = intent.getStringExtra("VEHICLE_TYPE") ?: "Desconhecido"

        // Exibir os dados
        parkingNameText.text = parkingName
        dateText.text = date
        spotIdText.text = spotId
        totalCostText.text = "€$totalCost"
        vehicleText.text = vehicle
        vehicleTypeText.text = vehicleType
    }
}
