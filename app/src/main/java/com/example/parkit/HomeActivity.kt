package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.databinding.ActivityHomeBinding
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val db = FirebaseFirestore.getInstance()

    private var selectedVehicleType: String = "Carro" // Tipo de veículo padrão
    private var priceCar: Double = 0.0
    private var priceBike: Double = 0.0
    private var priceVan: Double = 0.0
    private var priceScooter: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        // Inicializar View Binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchBar.setOnClickListener {
            val intent = Intent(this, ExploreActivity::class.java)
            startActivity(intent)
        }

        binding.profileImage.setOnClickListener {
            val intent = Intent(this, ProfileBeginActivity::class.java)
            startActivity(intent)
        }


        // Configurar botões de filtro
        setupFilterButtons()

        // Carregar dados do estacionamento
        loadParkingData()

        // Configurar clique no cartão
        setupParkingCardClickListener()

        binding.notificationButton.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupFilterButtons() {
        binding.carButton.setOnClickListener {
            selectedVehicleType = "Carro"
            updateParkingPrice()
        }
        binding.bikeButton.setOnClickListener {
            selectedVehicleType = "Moto"
            updateParkingPrice()
        }
        binding.vanButton.setOnClickListener {
            selectedVehicleType = "Van"
            updateParkingPrice()
        }
        binding.scooterButton.setOnClickListener {
            selectedVehicleType = "Scooter"
            updateParkingPrice()
        }
    }

    private fun loadParkingData() {
        db.collection("parkingPrices").document("parking1")
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Carregar strings
                    val name = document.getString("name") ?: "Desconhecido"
                    val address = document.getString("address") ?: "Sem endereço"

                    // Carregar preços
                    priceCar = document.getDouble("priceCar") ?: 0.0
                    priceBike = document.getDouble("priceBike") ?: 0.0
                    priceVan = document.getDouble("priceVan") ?: 0.0
                    priceScooter = document.getDouble("priceScooter") ?: 0.0

                    // Atualizar UI
                    binding.parkingName.text = name
                    binding.parkingAddress.text = address
                    updateParkingPrice()

                    Log.d("Firestore", "Nome: $name, Endereço: $address")
                    Log.d("Firestore", "Preços - Carro: $priceCar, Moto: $priceBike, Van: $priceVan, Scooter: $priceScooter")
                } else {
                    Log.e("Firestore", "Documento não encontrado.")
                    Toast.makeText(this, "Dados não encontrados no Firestore.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Erro ao carregar dados", exception)
                Toast.makeText(this, "Erro ao carregar os dados", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupParkingCardClickListener() {
        // Configurar clique no cartão de estacionamento
        binding.parkingCard.setOnClickListener {
            val intent = Intent(this, ChooseSpaceActivity::class.java)

            // Passar informações, se necessário
            intent.putExtra("parking_name", binding.parkingName.text.toString())
            intent.putExtra("parking_address", binding.parkingAddress.text.toString())
            intent.putExtra("parking_price", binding.parkingPrice.text.toString())

            // Iniciar a nova atividade
            startActivity(intent)
        }
    }

    private fun updateParkingPrice() {
        val adjustedPrice = when (selectedVehicleType) {
            "Carro" -> priceCar
            "Moto" -> priceBike
            "Van" -> priceVan
            "Scooter" -> priceScooter
            else -> 0.0
        }

        binding.parkingPrice.text = String.format("%.2f € / hora", adjustedPrice)
    }
}
