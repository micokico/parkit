package com.example.parkit

import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SpaceBookingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_space_booking)

        // Recuperando os dados passados pela ChooseSpaceActivity
        val selectedSpot = intent.getStringExtra("SELECTED_SPOT")
        val parkingName = intent.getStringExtra("PARKING_NAME")
        val parkingPrice = intent.getStringExtra("PARKING_PRICE")

        // Encontrar os TextViews onde os dados serão exibidos
        val tvSelectedSpace = findViewById<TextView>(R.id.tvSelectedSpace)
        val tvParkingName = findViewById<TextView>(R.id.tvParkingName)
        val tvParkingPrice = findViewById<TextView>(R.id.tvParkingPrice)

        // Atualizando os TextViews com os dados recebidos
        if (selectedSpot != null) {
            tvSelectedSpace.text = "Espaço: $selectedSpot"
        }

        if (parkingName != null) {
            tvParkingName.text = "Nome do Estacionamento: $parkingName"
        }

        if (parkingPrice != null) {
            tvParkingPrice.text = "Preço: $parkingPrice"
        }

        // Adicionando funcionalidade ao botão de reserva
        val bookButton = findViewById<Button>(R.id.btnBookSpace)
        bookButton.setOnClickListener {
            // Aqui você pode adicionar lógica de confirmação da reserva
        }
    }
}
