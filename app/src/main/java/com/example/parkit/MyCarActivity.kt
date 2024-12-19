package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class Vehicle(val id: String, val type: String, val name: String, val plate: String, val imageUrl: String?)

class MyCarActivity : AppCompatActivity() {

    private lateinit var addVehicleButton: Button
    private lateinit var removeVehicleButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mycar)

        // Botões
        addVehicleButton = findViewById(R.id.addVehicleButton)
        removeVehicleButton = findViewById(R.id.removeVehicleButton)

        // Carregar veículos
        loadVehicles()

        // Botão Adicionar Veículo
        addVehicleButton.setOnClickListener {
            startActivity(Intent(this, MyCarAdicionarCarroActivity::class.java))
        }

        // Botão Remover Veículo (placeholder)
        removeVehicleButton.setOnClickListener {
            Toast.makeText(this, "Funcionalidade de remoção ainda não implementada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadVehicles() {
        val database = FirebaseDatabase.getInstance().getReference("vehicles")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Atualizar UI com veículos
                updateVehicleList(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MyCarActivity, "Erro ao carregar veículos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateVehicleList(snapshot: DataSnapshot) {
        val vehiclesList = findViewById<LinearLayout>(R.id.vehiclesListLayout)
        vehiclesList.removeAllViews()

        for (vehicleSnapshot in snapshot.children) {
            val vehicle = vehicleSnapshot.getValue(Vehicle::class.java)
            if (vehicle != null) {
                val vehicleView = createVehicleView(vehicle)
                vehiclesList.addView(vehicleView)
            }
        }
    }

    private fun createVehicleView(vehicle: Vehicle): LinearLayout {
        val vehicleLayout = LinearLayout(this)
        vehicleLayout.orientation = LinearLayout.VERTICAL
        vehicleLayout.setPadding(16, 16, 16, 16)

        val vehicleName = TextView(this).apply {
            text = vehicle.name
            textSize = 16f
            setTextColor(resources.getColor(android.R.color.black, null))
        }

        val vehicleDetail = TextView(this).apply {
            text = vehicle.plate
            textSize = 14f
            setTextColor(resources.getColor(android.R.color.darker_gray, null))
        }

        vehicleLayout.addView(vehicleName)
        vehicleLayout.addView(vehicleDetail)

        return vehicleLayout
    }
}
