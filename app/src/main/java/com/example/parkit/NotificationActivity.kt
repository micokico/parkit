import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class NotificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = layoutInflater.inflate(resources.getIdentifier("activity_notification", "layout", packageName), null)
        setContentView(view)

        val backButton = findViewById<ImageButton>(resources.getIdentifier("btn_back", "id", packageName))

        backButton?.setOnClickListener {
            onBackPressed()
        }
    }
}
