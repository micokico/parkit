package com.example.parkit

import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ActivePermitActivity : AppCompatActivity() {

    private lateinit var permitAdapter: PermitAdapter
    private lateinit var switchShowExpired: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permite_page)

        // Inicializa as views
        val recyclerView: RecyclerView = findViewById(R.id.rvPermitList)
        switchShowExpired = findViewById(R.id.switchShowExpired)

        // Configura o RecyclerView
        permitAdapter = PermitAdapter(getPermitList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = permitAdapter

        // Configura o listener do Switch
        switchShowExpired.setOnCheckedChangeListener { _, isChecked ->
            val filteredList = if (isChecked) {
                getPermitList(includeExpired = true)
            } else {
                getPermitList(includeExpired = false)
            }
            permitAdapter.updateList(filteredList)
        }

        // Configura o botão de voltar
        val ivBackButton: ImageView = findViewById(R.id.ivBackButton)
        ivBackButton.setOnClickListener {
            onBackPressed()  // Volta para a Activity anterior
        }
    }

    private fun getPermitList(includeExpired: Boolean = false): List<Permit> {
        val permits = listOf(
            Permit("TEST123", "IB4 APS", "30-12-2022 15:16:02"),
            Permit("TEST124", "IB5 APS", "30-12-2023 15:16:02"),
            Permit("TEST125", "IB6 APS", "30-12-2021 15:16:02") // Expired
        )

        return if (includeExpired) {
            permits
        } else {
            permits.filter { !it.isExpired() }
        }
    }
}

// Data Model
data class Permit(val matricula: String, val lugarOcupado: String, val dataLimite: String) {
    fun isExpired(): Boolean {
        // Simula a verificação de expiração (a implementação real deve comparar as datas corretamente)
        return dataLimite.contains("2021")
    }
}

// RecyclerView Adapter
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
        tvDataLimite.text = permit.dataLimite
    }
}
