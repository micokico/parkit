package com.example.parkit

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        val profileButton = findViewById<Button>(R.id.profileButton)
        val myCarsButton = findViewById<Button>(R.id.myCarsButton)
        val historyButton = findViewById<Button>(R.id.historyButton)

        profileButton.setOnClickListener {
            // Load user profile
        }

        myCarsButton.setOnClickListener {
            // Navigate to My Cars screen
        }

        historyButton.setOnClickListener {
            // Navigate to History screen
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }
}
