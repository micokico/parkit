package com.example.parkit

import ParkingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ParkingAdapter
    private val recentList = mutableListOf<Parking>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.history)

        val searchEditText = findViewById<EditText>(R.id.historySearchEditText)
        recyclerView = findViewById(R.id.historyRecyclerView)

        adapter = ParkingAdapter(recentList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadRecentHistory()

        searchEditText.addTextChangedListener {
            val query = it.toString()
            filterResults(query)
        }
    }

    private fun loadRecentHistory() {
        val db = FirebaseFirestore.getInstance()
        db.collection("history").get()
            .addOnSuccessListener { result ->
                recentList.clear()
                for (document in result) {
                    val parking = document.toObject(Parking::class.java)
                    recentList.add(parking)
                }
                adapter.notifyDataSetChanged()
            }
    }

    private fun filterResults(query: String) {
        val filteredList = recentList.filter { it.name.contains(query, ignoreCase = true) }
        adapter.updateList(filteredList)
    }
}
