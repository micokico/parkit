package com.example.parkit

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
            bookSelectedSpot()
        }

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        loadParkingSpotsFromFirestore(currentFloor)
    }

    /**
     * Setup listeners for floor buttons
     */
    private fun setupFloorButtons() {
        floor1Button.setOnClickListener { setSelectedFloor(1) }
        floor2Button.setOnClickListener { setSelectedFloor(2) }
        floor3Button.setOnClickListener { setSelectedFloor(3) }
        floor4Button.setOnClickListener { setSelectedFloor(4) }
    }

    /**
     * Set the selected floor and update the button states
     */
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

    /**
     * Load parking spots from Firestore based on the selected floor
     */
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

    /**
     * Reset all parking spots to "free" state
     */
    private fun resetParkingSpots() {
        spots.values.forEach { spot ->
            spot.setImageResource(R.drawable.ic_free_spot)
            spot.setOnClickListener(null)
        }
    }

    /**
     * Updates a single parking spot state
     * @param spotId The ID of the parking spot (e.g., "A1", "B2")
     * @param state The state of the parking spot ("free", "car", "selected")
     */
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

    /**
     * Select a parking spot
     */
    private fun selectSpot(spotId: String) {

        selectedSpot?.let { deselectSpot(it) }

        selectedSpot = spotId
        Toast.makeText(this, "Selecionado: $spotId", Toast.LENGTH_SHORT).show()
        updateParkingSpot(spotId, "selected")
    }

    /**
     * Deselect a parking spot
     */
    private fun deselectSpot(spotId: String) {
        selectedSpot = null
        Toast.makeText(this, "Desselecionado: $spotId", Toast.LENGTH_SHORT).show()
        updateParkingSpot(spotId, "free")
    }

    /**
     * Book the selected parking spot
     */
    private fun bookSelectedSpot() {
        val spotId = selectedSpot
        if (spotId != null) {
            val floorKey = "floor_$currentFloor"
            firestore.collection("parkingSpots").document(floorKey)
                .update(spotId, "car")
                .addOnSuccessListener {
                    Toast.makeText(this, "Lugar reservado: $spotId", Toast.LENGTH_SHORT).show()
                    loadParkingSpotsFromFirestore(currentFloor)
                }
                .addOnFailureListener { error ->
                    Toast.makeText(this, "Erro ao reservar lugar: ${error.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Selecione um lugar primeiro!", Toast.LENGTH_SHORT).show()
        }
    }
}
