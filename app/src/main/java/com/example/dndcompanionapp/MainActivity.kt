package com.example.dndcompanionapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Connect this Kotlin file to the layout
        setContentView(R.layout.activity_main)

        // Find the button and text view by their ID
        val rollButton: Button = findViewById(R.id.rollButton)
        val resultText: TextView = findViewById(R.id.resultText)

        // Set up the click behavior
        rollButton.setOnClickListener {
            val roll = Random.nextInt(1, 21) // Generate a random number from 1 to 20
            resultText.text = "Result: $roll"
        }
    }
}
