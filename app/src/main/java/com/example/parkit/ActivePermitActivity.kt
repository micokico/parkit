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

        // Configurar o adaptador e RecyclerView
        permitAdapter = PermitAdapter(getPermitList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = permitAdapter

        // Listener para alternar exibição de expirados
        switchShowExpired.setOnCheckedChangeListener { _, isChecked ->
            val filteredList = if (isChecked) {
                getPermitList(includeExpired = true)
            } else {
                getPermitList(includeExpired = false)
            }
            permitAdapter.updateList(filteredList)
        }

        // Botão de voltar
        val ivBackButton: ImageView = findViewById(R.id.ivBackButton)
        ivBackButton.setOnClickListener {
            onBackPressed()
        }
    }

    // Função para gerar a lista de permissões
    private fun getPermitList(includeExpired: Boolean = false): List<Permit> {
        val permits = listOf(
            Permit("TEST123", "IB4 APS", "30-01-2024 15:16:02"),
            Permit("TEST124", "IB5 APS", "15-02-2025 15:16:02"),
            Permit("TEST126", "IB7 APS", "20-03-2025 15:16:02"),
            Permit("TEST127", "IB8 APS", "05-04-2024 15:16:02"),
            Permit("TEST128", "IB9 APS", "10-05-2025 15:16:02"),
            Permit("TEST129", "IB10 APS", "15-06-2025 15:16:02"),
            Permit("TEST130", "IB11 APS", "25-07-2024 15:16:02"),
            Permit("TEST125", "IB6 APS", "30-12-2023 15:16:02")
        )

        return if (includeExpired) {
            permits
        } else {
            permits.filter { !it.isExpired() }
        }
    }
}

// Classe de dados para as permissões
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

// Adaptador para o RecyclerView
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

// ViewHolder para exibir os dados da permissão
class PermitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvMatricula: TextView = itemView.findViewById(R.id.tvMatricula)
    private val tvLugarOcupado: TextView = itemView.findViewById(R.id.tvLugarOcupado)
    private val tvDataLimite: TextView = itemView.findViewById(R.id.tvDataLimite)

    fun bind(permit: Permit) {
        tvMatricula.text = permit.matricula
        tvLugarOcupado.text = permit.lugarOcupado
        if (permit.isExpired()) {
            tvDataLimite.text = "Data Expirada"
            tvDataLimite.setTextColor(itemView.context.getColor(R.color.red)) // Certifique-se de definir a cor "red" no arquivo colors.xml
        } else {
            tvDataLimite.text = permit.dataLimite
            tvDataLimite.setTextColor(itemView.context.getColor(R.color.black)) // Cor padrão
        }
    }
}
