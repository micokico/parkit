package com.example.parkit

import android.content.Intent
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

class ReservationAdapter(
    private val reservations: List<Reservation>,
    private val onCancelReservation: (Reservation) -> Unit // Função para cancelar reserva
) : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    inner class ReservationViewHolder(val view: android.view.View) :
        RecyclerView.ViewHolder(view) {
        val dateText: android.widget.TextView = view.findViewById(R.id.dateText)
        val parkingNameText: android.widget.TextView = view.findViewById(R.id.parkingNameText)
        val spotIdText: android.widget.TextView = view.findViewById(R.id.spotIdText)
        val totalCostText: android.widget.TextView = view.findViewById(R.id.totalCostText)
        val cancelButton: android.widget.Button = view.findViewById(R.id.btn_cancel_reservation)
        val bookingCard: androidx.cardview.widget.CardView = view.findViewById(R.id.bookingCard) // O CardView
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ReservationViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reserva_template, parent, false)
        return ReservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservations[position]
        holder.dateText.text = "Data: ${reservation.date}"
        holder.parkingNameText.text = "Nome do Parque: ${reservation.parkingName}"
        holder.spotIdText.text = "ID do Lugar: ${reservation.spotId}"
        holder.totalCostText.text = "Custo Total: €${reservation.totalCost}"

        // Configurar botão de cancelar
        holder.cancelButton.setOnClickListener {
            onCancelReservation(reservation)
        }

        // Configurar clique no CardView para abrir BookingDetailsActivity
        holder.bookingCard.setOnClickListener {
            val context = holder.view.context
            val intent = Intent(context, BookingDetailsActivity::class.java)
            intent.putExtra("PARKING_NAME", reservation.parkingName)
            intent.putExtra("DATE", reservation.date)
            intent.putExtra("SPOT_ID", reservation.spotId)
            intent.putExtra("TOTAL_COST", reservation.totalCost)
            intent.putExtra("VEHICLE", reservation.vehicle)
            intent.putExtra("VEHICLE_TYPE", reservation.vehicleType)
            context.startActivity(intent)
        }
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

        adapter = ReservationAdapter(reservations) { reservation ->
            cancelReservation(reservation)
        }
        recyclerView.adapter = adapter

        // Configurar botão de voltar
        val backButton = findViewById<ImageButton>(R.id.btn_back)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Buscar reservas do Firestore
        fetchReservations()
    }

    private fun fetchReservations() {
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

    private fun cancelReservation(reservation: Reservation) {
        db.collection("reservations")
            .whereEqualTo("date", reservation.date)
            .whereEqualTo("time", reservation.time)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    db.collection("reservations").document(document.id).delete()
                        .addOnSuccessListener {
                            Log.d("ReservationListActivity", "Reserva cancelada com sucesso.")
                            reservations.remove(reservation)
                            adapter.notifyDataSetChanged()
                        }
                        .addOnFailureListener { e ->
                            Log.e("ReservationListActivity", "Erro ao cancelar reserva.", e)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("ReservationListActivity", "Erro ao localizar reserva.", exception)
            }
    }
}


