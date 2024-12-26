package com.example.parkit

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class ChooseSpaceActivity : AppCompatActivity() {

    // Floor buttons
    private lateinit var floor1Button: ImageButton
    private lateinit var floor2Button: ImageButton
    private lateinit var floor3Button: ImageButton
    private lateinit var floor4Button: ImageButton

    // Parking spots
    private lateinit var spots: Map<String, ImageView>

    // Firebase Database reference
    private lateinit var database: DatabaseReference

    // Currently selected floor
    private var currentFloor: Int = 1

    // Selected parking spot
    private var selectedSpot: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_space)

        // Initialize Firebase reference
        database = FirebaseDatabase.getInstance().reference.child("parkingSpots")

        // Initialize floor buttons
        floor1Button = findViewById(R.id.floor1Button)
        floor2Button = findViewById(R.id.floor2Button)
        floor3Button = findViewById(R.id.floor3Button)
        floor4Button = findViewById(R.id.floor4Button)

        // Initialize all parking spots
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

        // Setup floor buttons with listeners
        setupFloorButtons()

        // Setup booking button
        val bookPlaceButton = findViewById<Button>(R.id.bookPlaceButton)
        bookPlaceButton.setOnClickListener {
            bookSelectedSpot()
        }

        // Setup back button
        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Close the current activity and return to the previous one
        }

        // Load initial data for Floor 1 from Firebase
        loadParkingSpotsFromFirebase(currentFloor)
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

        // Reset all buttons to unselected
        floor1Button.setImageResource(R.drawable.floor1_unselected)
        floor2Button.setImageResource(R.drawable.floor2_unselected)
        floor3Button.setImageResource(R.drawable.floor3_unselected)
        floor4Button.setImageResource(R.drawable.floor4_unselected)

        // Highlight the selected floor button
        when (selectedFloor) {
            1 -> floor1Button.setImageResource(R.drawable.floor1_selected)
            2 -> floor2Button.setImageResource(R.drawable.floor2_selected)
            3 -> floor3Button.setImageResource(R.drawable.floor3_selected)
            4 -> floor4Button.setImageResource(R.drawable.floor4_selected)
        }

        // Load parking spots for the selected floor from Firebase
        loadParkingSpotsFromFirebase(selectedFloor)
    }

    /**
     * Load parking spots from Firebase based on the selected floor
     */
    private fun loadParkingSpotsFromFirebase(floor: Int) {
        database.child("floor_$floor").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                resetParkingSpots() // Clear all spots before updating

                for (spotSnapshot in snapshot.children) {
                    val spotId = spotSnapshot.key ?: continue
                    val state = spotSnapshot.getValue(String::class.java) ?: "free"
                    updateParkingSpot(spotId, state)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ChooseSpaceActivity, "Error loading data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Reset all parking spots to "free" state
     */
    private fun resetParkingSpots() {
        spots.values.forEach { spot ->
            spot.setImageResource(R.drawable.ic_free_spot)
            spot.setOnClickListener(null) // Remove click listeners
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
                "car" -> spot.setImageResource(R.drawable.ic_car)
                "selected" -> spot.setImageResource(R.drawable.ic_selected_spot)
            }
        }
    }

    /**
     * Select a parking spot
     */
    private fun selectSpot(spotId: String) {
        selectedSpot = spotId
        Toast.makeText(this, "Selecionado: $spotId", Toast.LENGTH_SHORT).show()
        updateParkingSpot(spotId, "selected")
    }

    /**
     * Book the selected parking spot
     */
    private fun bookSelectedSpot() {
        val spotId = selectedSpot
        if (spotId != null) {
            database.child("floor_$currentFloor").child(spotId).setValue("car")
                .addOnSuccessListener {
                    Toast.makeText(this, "Lugar reservado: $spotId", Toast.LENGTH_SHORT).show()
                    loadParkingSpotsFromFirebase(currentFloor) // Refresh the floor
                }
                .addOnFailureListener { error ->
                    Toast.makeText(this, "Erro ao reservar lugar: ${error.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Selecione um lugar primeiro!", Toast.LENGTH_SHORT).show()
        }
    }
}
