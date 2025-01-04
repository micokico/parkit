package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.databinding.ActivityHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val db = FirebaseFirestore.getInstance()

    private var selectedVehicleType: String = "Carro"
    private var priceCar: Double = 0.0
    private var priceBike: Double = 0.0
    private var priceVan: Double = 0.0
    private var priceScooter: Double = 0.0
    private var userName: String = "Nome"

    private var userListener: ListenerRegistration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadUserData()

        binding.searchBar.setOnClickListener {
            val intent = Intent(this, ExploreActivity::class.java)
            startActivity(intent)
        }

        binding.profileImage.setOnClickListener {
            val intent = Intent(this, ProfileBeginActivity::class.java)
            startActivity(intent)
        }

        setupFilterButtons()

        loadParkingData()

        setupParkingCardClickListener()

        binding.notificationButton.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        userListener?.remove()
    }

    private fun loadUserData() {
        val phoneNumber = "918235917"
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            userListener = db.collection("users")
                .whereEqualTo("phoneNumber", phoneNumber)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        Log.e("Firestore", "Erro ao carregar dados", exception)
                        Toast.makeText(this, "Erro ao carregar os dados", Toast.LENGTH_SHORT).show()
                        return@addSnapshotListener
                    }

                    if (snapshot != null && !snapshot.isEmpty) {
                        val document = snapshot.documents.firstOrNull()
                        userName = document?.getString("name") ?: "Usuário"

                        binding.greetingText.text = "Bom dia, $userName"

                        Log.d("Firestore", "Nome do usuário atualizado: $userName")
                    } else {
                        Log.e("Firestore", "Usuário não encontrado.")
                        Toast.makeText(this, "Usuário não encontrado.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Log.e("FirebaseAuth", "Usuário não autenticado.")
            Toast.makeText(this, "Usuário não autenticado.", Toast.LENGTH_SHORT).show()
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
                    val name = document.getString("name") ?: "Desconhecido"
                    val address = document.getString("address") ?: "Sem endereço"

                    priceCar = document.getDouble("priceCar") ?: 0.0
                    priceBike = document.getDouble("priceBike") ?: 0.0
                    priceVan = document.getDouble("priceVan") ?: 0.0
                    priceScooter = document.getDouble("priceScooter") ?: 0.0

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
        binding.parkingCard.setOnClickListener {
            val adjustedPrice = when (selectedVehicleType) {
                "Carro" -> priceCar
                "Moto" -> priceBike
                "Van" -> priceVan
                "Scooter" -> priceScooter
                else -> 0.0
            }

            val intent = Intent(this, ChooseSpaceActivity::class.java)
            intent.putExtra("PARKING_NAME", binding.parkingName.text.toString())
            intent.putExtra("PARKING_ADDRESS", binding.parkingAddress.text.toString())
            intent.putExtra("VEHICLE_TYPE", selectedVehicleType)
            intent.putExtra("VEHICLE_PRICE", adjustedPrice)
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
