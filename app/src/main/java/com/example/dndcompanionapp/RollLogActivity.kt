package com.example.dndcompanionapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class RollLogActivity : AppCompatActivity() {

    private lateinit var logTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_roll_log)

        logTextView = findViewById(R.id.logTextView)

        val entries = RollLog.getEntries()
        val logText = if (entries.isEmpty()) {
            "No rolls yet."
        } else {
            entries.joinToString("\n\n") { entry ->
                "Total: ${entry.total}\nBreakdown:\n${entry.breakdown}"
            }
        }

        logTextView.text = logText
    }
}
