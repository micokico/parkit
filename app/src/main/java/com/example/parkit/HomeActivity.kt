package com.example.parkit

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parkit.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityHomeBinding

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

        binding.notificationButton.setOnClickListener {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }

        // Configurar barra de pesquisa
        setupSearchBar()

        // Configurar botões de filtro
        setupFilterButtons()

        // Carregar estacionamentos próximos
        loadNearbyParking()

        // Configurar navegação inferior
        setupBottomNavigation()
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

    private fun setupSearchBar() {
        binding.searchEditText.setOnEditorActionListener { _, _, _ ->
            val query = binding.searchEditText.text.toString()
            if (query.isNotEmpty()) {
                Toast.makeText(this, "Procurando por: $query", Toast.LENGTH_SHORT).show()
                // Implementar lógica de pesquisa
            }
            true
        }
    }

    private fun setupFilterButtons() {
        binding.carButton.setOnClickListener {
            Toast.makeText(this, "Filtro: Carro", Toast.LENGTH_SHORT).show()
            // Lógica do filtro de carro
        }

        binding.bikeButton.setOnClickListener {
            Toast.makeText(this, "Filtro: Moto", Toast.LENGTH_SHORT).show()
            // Lógica do filtro de moto
        }

        binding.vanButton.setOnClickListener {
            Toast.makeText(this, "Filtro: Van", Toast.LENGTH_SHORT).show()
            // Lógica do filtro de van
        }

        binding.scooterButton.setOnClickListener {
            Toast.makeText(this, "Filtro: Scooter", Toast.LENGTH_SHORT).show()
            // Lógica do filtro de scooter
        }
    }

    private fun loadNearbyParking() {
        // Simulando carregamento de dados do Firebase
        database.child("parking").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (parkingSnapshot in snapshot.children) {
                    val name = parkingSnapshot.child("name").value as? String ?: "Desconhecido"
                    val address = parkingSnapshot.child("address").value as? String ?: "Sem endereço"
                    val price = parkingSnapshot.child("price").value as? String ?: "N/A"

                    binding.parkingName.text = name
                    binding.parkingAddress.text = address
                    binding.parkingPrice.text = price
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HomeActivity, "Erro ao carregar os dados", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home selecionado", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_navigation -> {
                    Toast.makeText(this, "Perfil selecionado", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_settings -> {
                    Toast.makeText(this, "Configurações selecionadas", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}
