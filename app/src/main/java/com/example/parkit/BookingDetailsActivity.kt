package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BookingDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)

        // Receber os dados da Intent
        val parkingName = intent.getStringExtra("PARKING_NAME")
        val spotId = intent.getStringExtra("SPOT_ID")
        val vehicle = intent.getStringExtra("VEHICLE")
        val vehicleType = intent.getStringExtra("VEHICLE_TYPE")
        val totalCost = intent.getDoubleExtra("TOTAL_COST", 0.0)
        val duration = intent.getIntExtra("DURATION", 0)
        val checkInTime = intent.getStringExtra("CHECK_IN_TIME")
        val checkOutTime = intent.getStringExtra("CHECK_OUT_TIME")
        val specifications = intent.getStringExtra("SPECIFICATIONS")

        // Referências para os Views
        val tvParkingName = findViewById<TextView>(R.id.tvParkingName)
        val tvSpaceName = findViewById<TextView>(R.id.tvSpaceName)
        val ivQRCode = findViewById<ImageView>(R.id.ivQRCode)
        val tvUniqueID = findViewById<TextView>(R.id.tvUniqueID)
        val tvCheckInTime = findViewById<TextView>(R.id.tvCheckInTime)
        val tvCheckOutTime = findViewById<TextView>(R.id.tvCheckOutTime)
        val tvSpecifications = findViewById<TextView>(R.id.tvSpecifications)

        // Definir os valores no layout
        tvParkingName.text = parkingName
        tvSpaceName.text = spotId
        tvUniqueID.text = "Unique ID: $spotId" // Gerar um Unique ID de forma personalizada se necessário
        tvCheckInTime.text = checkInTime ?: "N/A"
        tvCheckOutTime.text = checkOutTime ?: "N/A"
        tvSpecifications.text = specifications ?: "Nenhuma"

        // Exibição do QR Code
        ivQRCode.setImageResource(R.drawable.ic_qr_code)  // Substitua pelo seu ícone ou lógica para gerar QR Code

        // Botão para voltar à HomeActivity
        val btnBackToHome = findViewById<Button>(R.id.btnBackToHome)
        btnBackToHome.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun openMapForDirections(parkingName: String) {
        // Lógica para abrir o Google Maps ou outro serviço de mapa
        // Exemplo de um intent que abre o Google Maps para o endereço de um estacionamento
        val geoUri = "geo:0,0?q=$parkingName"
        val intent = Intent(Intent.ACTION_VIEW, android.net.Uri.parse(geoUri))
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }
}
