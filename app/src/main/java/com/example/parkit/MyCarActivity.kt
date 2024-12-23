package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.*

data class Vehicle(
    val id: String = "",
    val type: String = "",
    val name: String = "",
    val plate: String = "",
    val imageUrl: String? = null
)

class MyCarActivity : AppCompatActivity() {

    private lateinit var addVehicleButton: Button
    private lateinit var removeVehicleButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var vehiclesListLayout: LinearLayout
    private val firestore = FirebaseFirestore.getInstance()

    // Lista de veículos selecionados para remoção
    private val selectedVehicles = mutableSetOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mycar)

        // Inicializar componentes da UI
        backButton = findViewById(R.id.btn_back)
        addVehicleButton = findViewById(R.id.addVehicleButton)
        removeVehicleButton = findViewById(R.id.removeVehicleButton)
        vehiclesListLayout = findViewById(R.id.vehiclesListLayout)

        // Configurar botão "Voltar"
        backButton.setOnClickListener {
            val intent = Intent(this, ProfileBeginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Carregar veículos do Firestore
        loadVehicles()

        // Botão de adicionar veículo
        addVehicleButton.setOnClickListener {
            startActivity(Intent(this, MyCarAdicionarCarroActivity::class.java))
        }

        // Configurar o botão "Remover Veículos"
        removeVehicleButton.setOnClickListener {
            removeSelectedVehicles()
        }
    }

    private fun loadVehicles() {
        firestore.collection("Vehicle")
            .get()
            .addOnSuccessListener { result ->
                vehiclesListLayout.removeAllViews() // Limpar layout antes de adicionar novos veículos
                if (result.isEmpty) {
                    Log.e("Firestore", "No vehicles found.")
                    Toast.makeText(this@MyCarActivity, "Nenhum veículo encontrado.", Toast.LENGTH_SHORT).show()
                } else {
                    for (document in result) {
                        val vehicle = document.toObject(Vehicle::class.java)
                        Log.d("Firestore", "Loaded vehicle: $vehicle")
                        vehiclesListLayout.addView(createVehicleView(vehicle, document.id))
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error loading vehicles: ${exception.message}")
                Toast.makeText(this@MyCarActivity, "Erro ao carregar veículos.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun createVehicleView(vehicle: Vehicle, documentId: String): LinearLayout {
        val vehicleLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(16, 16, 16, 16)
        }

        val vehicleDetailsLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 0, 0, 0)
        }

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

        val vehicleImage = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(100, 100)
            scaleType = ImageView.ScaleType.CENTER_CROP

            if (!vehicle.imageUrl.isNullOrEmpty()) {
                Glide.with(this@MyCarActivity)
                    .load(vehicle.imageUrl)
                    .placeholder(R.drawable.carro) // Placeholder padrão
                    .into(this)
            } else {
                setImageResource(R.drawable.carro) // Ícone padrão se imageUrl for null
            }
        }

        // CheckBox para selecionar veículo
        val selectCheckBox = CheckBox(this).apply {
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedVehicles.add(documentId)  // Adiciona à lista de veículos selecionados
                } else {
                    selectedVehicles.remove(documentId)  // Remove da lista de veículos selecionados
                }
            }
        }

        vehicleDetailsLayout.addView(vehicleName)
        vehicleDetailsLayout.addView(vehicleDetail)

        vehicleLayout.addView(vehicleImage)
        vehicleLayout.addView(vehicleDetailsLayout)
        vehicleLayout.addView(selectCheckBox)

        return vehicleLayout
    }

    private fun removeSelectedVehicles() {
        if (selectedVehicles.isEmpty()) {
            Toast.makeText(this, "Selecione pelo menos um veículo para remover.", Toast.LENGTH_SHORT).show()
            return
        }

        selectedVehicles.forEach { documentId ->
            firestore.collection("Vehicle").document(documentId)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Veículo removido com sucesso.", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Log.e("Firestore", "Error removing vehicle: ${exception.message}")
                    Toast.makeText(this, "Erro ao remover veículo.", Toast.LENGTH_SHORT).show()
                }
        }

        selectedVehicles.clear()
        loadVehicles()
    }
}

