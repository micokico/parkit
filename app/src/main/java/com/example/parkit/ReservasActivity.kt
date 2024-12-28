package com.example.parkit

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

data class Reservation(
    val date: String = "",
    val duration: Int = 0,
    val parkingName: String = "",
    val spotId: String = "",
    val time: String = "",
    val totalCost: Int = 0,
    val vehicle: String = "",
    val vehicleType: String = ""
)

class ReservationAdapter(private val reservations: List<Reservation>) :
    RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    // ViewHolder implementation
    inner class ReservationViewHolder(val view: android.view.View) :
        RecyclerView.ViewHolder(view) {
        val dateText: android.widget.TextView = view.findViewById(R.id.dateText)
        val parkingNameText: android.widget.TextView = view.findViewById(R.id.parkingNameText)
        val spotIdText: android.widget.TextView = view.findViewById(R.id.spotIdText)
        val totalCostText: android.widget.TextView = view.findViewById(R.id.totalCostText)

    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ReservationViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reserva_template, parent, false)
        return ReservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservations[position]
        holder.dateText.text = "Date: ${reservation.date}"
        holder.parkingNameText.text = "Parking: ${reservation.parkingName}"
        holder.spotIdText.text = "Spot ID: ${reservation.spotId}"
        holder.totalCostText.text = "Cost: \$${reservation.totalCost}"
    }

    override fun getItemCount(): Int = reservations.size
}


class ReservationListActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReservationAdapter
    private val reservations = mutableListOf<Reservation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservas)

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ReservationAdapter(reservations)
        recyclerView.adapter = adapter

        // Configurar botão de voltar
        val backButton = findViewById<ImageButton>(R.id.btn_back)
        backButton.setOnClickListener {
            onBackPressed() // Chama o comportamento padrão de voltar
        }

        // Buscar reservas do Firestore
        fetchReservations()
    }

    private fun fetchReservations() {
        val phoneNumber = "918235917" // Substituir pelo número de telefone do utilizador atual.

        db.collection("reservations")
            .get()
            .addOnSuccessListener { result ->
                reservations.clear()
                for (document in result) {
                    try {
                        val reservation = document.toObject(Reservation::class.java)
                        reservations.add(reservation)
                    } catch (e: Exception) {
                        Log.e("ReservationListActivity", "Erro ao converter documento: ${document.id}", e)
                    }
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("ReservationListActivity", "Erro ao buscar reservas", exception)
            }
    }
}

