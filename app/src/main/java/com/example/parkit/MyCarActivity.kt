package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import android.view.Gravity


data class Vehicle(
    val id: String = "",
    val type: String = "",
    val name: String = "",
    val plate: String = ""
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

        // Botão "Remover Veículos"
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
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                marginStart = 16 // Desloca o texto para a direita
            }
        }

        val vehicleDetail = TextView(this).apply {
            text = vehicle.plate
            textSize = 14f
            setTextColor(resources.getColor(android.R.color.darker_gray, null))
        }

        val vehicleImage = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(80, 80).apply {
                gravity = Gravity.CENTER_VERTICAL // Centraliza a imagem verticalmente no layout
            }
            scaleType = ImageView.ScaleType.FIT_CENTER // Mantém a proporção da imagem

            // Definir a imagem com base no tipo de veículo
            when (vehicle.type.lowercase()) {
                "carro" -> setImageResource(R.drawable.carro)
                "moto" -> setImageResource(R.drawable.mota)
                "van" -> setImageResource(R.drawable.van)
                "scooter" -> setImageResource(R.drawable.scotter)
                else -> setImageResource(R.drawable.carro) // Imagem padrão para tipos desconhecidos
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

        // Remover os veículos selecionados
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

        // Limpar a lista de veículos selecionados
        selectedVehicles.clear()

        // Recarregar os veículos após remoção
        loadVehicles()
    }
}
