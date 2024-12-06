import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.parkit.R

class BookingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        // Referências dos slots
        val slots = listOf(
            findViewById<ImageView>(R.id.slot_1a),
            findViewById<ImageView>(R.id.slot_2a),
            findViewById<ImageView>(R.id.slot_3a),
            findViewById<ImageView>(R.id.slot_4a),
            findViewById<ImageView>(R.id.slot_5a),
            findViewById<ImageView>(R.id.slot_6a),

            findViewById<ImageView>(R.id.slot_1b),
            findViewById<ImageView>(R.id.slot_2b),
            findViewById<ImageView>(R.id.slot_3b),
            findViewById<ImageView>(R.id.slot_4b),
            findViewById<ImageView>(R.id.slot_5b),
            findViewById<ImageView>(R.id.slot_6b),

            findViewById<ImageView>(R.id.slot_1c),
            findViewById<ImageView>(R.id.slot_2c),
            findViewById<ImageView>(R.id.slot_3c),
            findViewById<ImageView>(R.id.slot_4c),
            findViewById<ImageView>(R.id.slot_5c),
            findViewById<ImageView>(R.id.slot_6c),

            findViewById<ImageView>(R.id.slot_1d),
            findViewById<ImageView>(R.id.slot_2d),
            findViewById<ImageView>(R.id.slot_3d),
            findViewById<ImageView>(R.id.slot_4d),
            findViewById<ImageView>(R.id.slot_5d),
            findViewById<ImageView>(R.id.slot_6d),
        )

        // Mapa para armazenar os estados dos slots
        val slotStates = mutableMapOf<Int, String>().apply {
            slots.forEachIndexed { index, slot ->
                val id = slot.id
                this[id] = if (index % 3 == 0) "occupied" else "free" // Exemplo de ocupação
            }
        }

        // Função para alterar a cor de acordo com o estado
        fun updateSlotColor(slot: ImageView, state: String) {
            val color = when (state) {
                "free" -> R.color.green  // Slot livre
                "occupied" -> R.color.black  // Slot ocupado
                "selected" -> R.color.blue  // Slot selecionado
                else -> R.color.gray  // Estado desconhecido
            }
            slot.setColorFilter(ContextCompat.getColor(this, color))
        }

        // Atualiza as cores iniciais dos slots
        slots.forEach { slot ->
            updateSlotColor(slot, slotStates[slot.id]!!)
        }

        // Clique em cada slot para alterar o estado
        slots.forEach { slot ->
            slot.setOnClickListener {
                val currentState = slotStates[slot.id]!!
                if (currentState == "occupied") {
                    Toast.makeText(this, "Este slot está ocupado!", Toast.LENGTH_SHORT).show()
                } else {
                    slotStates[slot.id] = if (currentState == "free") "selected" else "free"
                    updateSlotColor(slot, slotStates[slot.id]!!)
                }
            }
        }
    }
}
