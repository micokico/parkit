package com.example.parkit

import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class ActivePermitActivity : AppCompatActivity() {

    private lateinit var permitAdapter: PermitAdapter
    private lateinit var switchShowExpired: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permite_page)

        val recyclerView: RecyclerView = findViewById(R.id.rvPermitList)
        switchShowExpired = findViewById(R.id.switchShowExpired)

        permitAdapter = PermitAdapter(getPermitList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = permitAdapter

        switchShowExpired.setOnCheckedChangeListener { _, isChecked ->
            val filteredList = if (isChecked) {
                getPermitList(includeExpired = true)
            } else {
                getPermitList(includeExpired = false)
            }
            permitAdapter.updateList(filteredList)
        }

        val ivBackButton: ImageView = findViewById(R.id.ivBackButton)
        ivBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getPermitList(includeExpired: Boolean = false): List<Permit> {
        val permits = listOf(
            Permit("BA-54-G4", "1ºPiso, B3", "30-01-2024 15:16:02"),
            Permit("GF-42-1R", "2ºPiso, C1", "15-02-2025 15:16:02"),
            Permit("HG-TY-63", "1ºPiso, A5", "20-03-2025 15:16:02"),
            Permit("VB-76-1R", "4ºPiso, D5", "05-04-2024 15:16:02"),
            Permit("B7-DV-17", "1ºPiso, A4", "10-05-2025 15:16:02"),
            Permit("NV-B9-17", "3ºPiso, B4", "15-06-2025 15:16:02"),
            Permit("18-GV-1T", "2ºPiso, C5", "25-07-2024 15:16:02"),
            Permit("HG-45-6J", "1ºPiso, C4", "30-12-2023 15:16:02")
        )

        return if (includeExpired) {
            permits
        } else {
            permits.filter { !it.isExpired() }
        }
    }
}

data class Permit(val matricula: String, val lugarOcupado: String, val dataLimite: String) {
    fun isExpired(): Boolean {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        return try {
            val expirationDate = dateFormat.parse(dataLimite)
            expirationDate.before(Date())
        } catch (e: Exception) {
            false
        }
    }
}

class PermitAdapter(private var permitList: List<Permit>) : RecyclerView.Adapter<PermitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PermitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_permit, parent, false)
        return PermitViewHolder(view)
    }

    override fun onBindViewHolder(holder: PermitViewHolder, position: Int) {
        holder.bind(permitList[position])
    }

    override fun getItemCount(): Int = permitList.size

    fun updateList(newList: List<Permit>) {
        permitList = newList
        notifyDataSetChanged()
    }
}

class PermitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvMatricula: TextView = itemView.findViewById(R.id.tvMatricula)
    private val tvLugarOcupado: TextView = itemView.findViewById(R.id.tvLugarOcupado)
    private val tvDataLimite: TextView = itemView.findViewById(R.id.tvDataLimite)

    fun bind(permit: Permit) {
        tvMatricula.text = permit.matricula
        tvLugarOcupado.text = permit.lugarOcupado
        if (permit.isExpired()) {
            tvDataLimite.text = "Data Expirada"
            tvDataLimite.setTextColor(itemView.context.getColor(R.color.red))
        } else {
            tvDataLimite.text = permit.dataLimite
            tvDataLimite.setTextColor(itemView.context.getColor(R.color.black))
        }
    }
}
