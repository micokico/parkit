package com.example.parkit

import ParkingAdapter
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ExploreActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ParkingAdapter
    private val parkingList = mutableListOf<Parking>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explore)

        val searchEditText = findViewById<EditText>(R.id.searchEditText)
        recyclerView = findViewById(R.id.exploreRecyclerView)

        adapter = ParkingAdapter(parkingList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadParkingFromFirebase()

        searchEditText.addTextChangedListener {
            val query = it.toString()
            filterResults(query)
        }
    }

    private fun loadParkingFromFirebase() {
        val db = FirebaseFirestore.getInstance()
        db.collection("parking").get()
            .addOnSuccessListener { result ->
                parkingList.clear()
                for (document in result) {
                    val parking = document.toObject(Parking::class.java)
                    parkingList.add(parking)
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun filterResults(query: String) {
        val filteredList = parkingList.filter { it.name.contains(query, ignoreCase = true) }
        adapter.updateList(filteredList)
    }
}
