package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class ChooseSpaceActivity : AppCompatActivity() {

    private lateinit var floor1Button: ImageButton
    private lateinit var floor2Button: ImageButton
    private lateinit var floor3Button: ImageButton
    private lateinit var floor4Button: ImageButton

    private lateinit var spots: Map<String, ImageView>

    private val firestore = FirebaseFirestore.getInstance()

    private var currentFloor: Int = 1
    private var selectedSpot: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_space)

        val parkingName = intent.getStringExtra("PARKING_NAME") ?: ""
        val parkingAddress = intent.getStringExtra("PARKING_ADDRESS") ?: ""
        val vehicleType = intent.getStringExtra("VEHICLE_TYPE") ?: ""
        val vehiclePrice = intent.getDoubleExtra("VEHICLE_PRICE", 0.0)

        Toast.makeText(this, "Parque: $parkingName, Veículo: $vehicleType", Toast.LENGTH_SHORT).show()

        floor1Button = findViewById(R.id.floor1Button)
        floor2Button = findViewById(R.id.floor2Button)
        floor3Button = findViewById(R.id.floor3Button)
        floor4Button = findViewById(R.id.floor4Button)

        spots = mapOf(
            "A1" to findViewById(R.id.spotA1),
            "A2" to findViewById(R.id.spotA2),
            "A3" to findViewById(R.id.spotA3),
            "A4" to findViewById(R.id.spotA4),
            "A5" to findViewById(R.id.spotA5),
            "B1" to findViewById(R.id.spotB1),
            "B2" to findViewById(R.id.spotB2),
            "B3" to findViewById(R.id.spotB3),
            "B4" to findViewById(R.id.spotB4),
            "B5" to findViewById(R.id.spotB5),
            "C1" to findViewById(R.id.spotC1),
            "C2" to findViewById(R.id.spotC2),
            "C3" to findViewById(R.id.spotC3),
            "C4" to findViewById(R.id.spotC4),
            "C5" to findViewById(R.id.spotC5),
            "D1" to findViewById(R.id.spotD1),
            "D2" to findViewById(R.id.spotD2),
            "D3" to findViewById(R.id.spotD3),
            "D4" to findViewById(R.id.spotD4),
            "D5" to findViewById(R.id.spotD5)
        )

        setupFloorButtons()

        val bookPlaceButton = findViewById<Button>(R.id.bookPlaceButton)
        bookPlaceButton.setOnClickListener {
            if (selectedSpot != null) {
                navigateToBooking(selectedSpot!!, parkingName, parkingAddress, vehicleType, vehiclePrice)
            } else {
                Toast.makeText(this, "Selecione um lugar primeiro!", Toast.LENGTH_SHORT).show()
            }
        }

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        loadParkingSpotsFromFirestore(currentFloor)
    }

    private fun setupFloorButtons() {
        floor1Button.setOnClickListener { setSelectedFloor(1) }
        floor2Button.setOnClickListener { setSelectedFloor(2) }
        floor3Button.setOnClickListener { setSelectedFloor(3) }
        floor4Button.setOnClickListener { setSelectedFloor(4) }
    }

    private fun setSelectedFloor(selectedFloor: Int) {
        currentFloor = selectedFloor

        floor1Button.setImageResource(R.drawable.floor1_unselected)
        floor2Button.setImageResource(R.drawable.floor2_unselected)
        floor3Button.setImageResource(R.drawable.floor3_unselected)
        floor4Button.setImageResource(R.drawable.floor4_unselected)

        when (selectedFloor) {
            1 -> floor1Button.setImageResource(R.drawable.floor1_selected)
            2 -> floor2Button.setImageResource(R.drawable.floor2_selected)
            3 -> floor3Button.setImageResource(R.drawable.floor3_selected)
            4 -> floor4Button.setImageResource(R.drawable.floor4_selected)
        }

        loadParkingSpotsFromFirestore(selectedFloor)
    }

    private fun loadParkingSpotsFromFirestore(floor: Int) {
        val floorKey = "floor_$floor"
        firestore.collection("parkingSpots").document(floorKey).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    resetParkingSpots()
                    document.data?.forEach { (spotId, state) ->
                        val spotState = state as? String ?: "free"
                        updateParkingSpot(spotId, spotState)
                    }
                } else {
                    Toast.makeText(this, "Nenhum dado encontrado para $floorKey", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Erro ao carregar dados: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun resetParkingSpots() {
        spots.values.forEach { spot ->
            spot.setImageResource(R.drawable.ic_free_spot)
            spot.setOnClickListener(null)
        }
    }

    private fun updateParkingSpot(spotId: String, state: String) {
        val spot = spots[spotId]
        if (spot != null) {
            when (state) {
                "free" -> {
                    spot.setImageResource(R.drawable.ic_free_spot)
                    spot.setOnClickListener {
                        selectSpot(spotId)
                    }
                }
                "car" -> {
                    spot.setImageResource(R.drawable.ic_car)
                    spot.setOnClickListener(null)
                }
                "selected" -> {
                    spot.setImageResource(R.drawable.ic_selected_spot)
                    spot.setOnClickListener {
                        deselectSpot(spotId)
                    }
                }
            }
        }
    }

    private fun selectSpot(spotId: String) {
        selectedSpot?.let { deselectSpot(it) }
        selectedSpot = spotId
        Toast.makeText(this, "Selecionado: $spotId", Toast.LENGTH_SHORT).show()
        updateParkingSpot(spotId, "selected")
    }

    private fun deselectSpot(spotId: String) {
        selectedSpot = null
        updateParkingSpot(spotId, "free")
    }

    private fun navigateToBooking(selectedSpot: String, parkingName: String, parkingAddress: String, vehicleType: String, vehiclePrice: Double) {
        val intent = Intent(this, SpaceBookingActivity::class.java)
        intent.putExtra("SELECTED_SPOT", selectedSpot)
        intent.putExtra("PARKING_NAME", parkingName)
        intent.putExtra("PARKING_ADDRESS", parkingAddress)
        intent.putExtra("VEHICLE_TYPE", vehicleType)
        intent.putExtra("PARKING_PRICE", vehiclePrice)
        intent.putExtra("CURRENT_FLOOR", currentFloor)
        startActivity(intent)
    }
}
