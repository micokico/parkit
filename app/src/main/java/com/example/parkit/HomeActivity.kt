package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityHomeBinding

    private var selectedVehicleType: String = "Carro" // Tipo de veículo padrão
    private var priceCar: Double = 0.0
    private var priceBike: Double = 0.0
    private var priceVan: Double = 0.0
    private var priceScooter: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar View Binding
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Configurar saudação do usuário
        setUserGreeting()

        // Configurar botões de filtro
        setupFilterButtons()

        // Carregar dados do estacionamento
        loadParkingData()

        binding.notificationButton.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUserGreeting() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            database.child("users").child(userId).child("name").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.value as? String ?: "Usuário"
                    binding.greetingText.text = "Bom dia, $name"
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@HomeActivity, "Erro ao carregar nome do usuário", Toast.LENGTH_SHORT).show()
                }
            })
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
        database.child("parking").child("parking1").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("HomeActivity", "Dados do estacionamento: $snapshot")

                val name = snapshot.child("name").value as? String ?: "Desconhecido"
                val address = snapshot.child("address").value as? String ?: "Sem endereço"
                priceCar = snapshot.child("priceCar").value as? Double ?: 0.0
                priceBike = snapshot.child("priceBike").value as? Double ?: 0.0
                priceVan = snapshot.child("priceVan").value as? Double ?: 0.0
                priceScooter = snapshot.child("priceScooter").value as? Double ?: 0.0

                Log.d("HomeActivity", "Preço Carro: $priceCar")
                Log.d("HomeActivity", "Preço Moto: $priceBike")
                Log.d("HomeActivity", "Preço Van: $priceVan")
                Log.d("HomeActivity", "Preço Scooter: $priceScooter")

                binding.parkingName.text = name
                binding.parkingAddress.text = address

                updateParkingPrice()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, "Erro ao carregar os dados do estacionamento", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateParkingPrice() {
        Log.d("HomeActivity", "Atualizando preço para: $selectedVehicleType")

        val adjustedPrice = when (selectedVehicleType) {
            "Carro" -> priceCar
            "Moto" -> priceBike
            "Van" -> priceVan
            "Scooter" -> priceScooter
            else -> 0.0
        }

        binding.parkingPrice.text = "$adjustedPrice € / hora"
    }
}
