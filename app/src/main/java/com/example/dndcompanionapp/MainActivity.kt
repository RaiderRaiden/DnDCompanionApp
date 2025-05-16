package com.example.dndcompanionapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var rollLogButton: Button
    private lateinit var rollButton: Button
    private lateinit var clearButton: Button
    private lateinit var diceButtonsLayout: LinearLayout
    private lateinit var resultText: TextView
    private lateinit var detailedRollsText: TextView

    private val diceMap = mapOf(
        R.id.d100Button to 100,
        R.id.d20Button to 20,
        R.id.d12Button to 12,
        R.id.d10Button to 10,
        R.id.d8Button to 8,
        R.id.d6Button to 6,
        R.id.d4Button to 4
    )

    private val selectedDice = mutableMapOf<Int, Int>() // die sides -> count

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        rollLogButton = findViewById(R.id.rollLogButton)
        rollButton = findViewById(R.id.rollButton)
        clearButton = findViewById(R.id.clearButton)
        diceButtonsLayout = findViewById(R.id.diceButtonsLayout)
        resultText = findViewById(R.id.resultText)
        detailedRollsText = findViewById(R.id.detailedRollsText)

        // Setup dice buttons click listeners
        for ((id, sides) in diceMap) {
            findViewById<Button>(id).setOnClickListener {
                addDie(sides)
            }
        }

        rollButton.setOnClickListener {
            rollDice()
        }

        clearButton.setOnClickListener {
            clearDice()
        }

        rollLogButton.setOnClickListener {
            val intent = Intent(this, RollLogActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addDie(sides: Int) {
        // If results are visible, assume a previous roll has happened, so start fresh
        if (resultText.visibility == View.VISIBLE) {
            selectedDice.clear()
            resultText.text = ""
            resultText.visibility = View.INVISIBLE
            detailedRollsText.text = ""
        }

        // Add the new die selection
        selectedDice[sides] = (selectedDice[sides] ?: 0) + 1
        updateDetailedRollsText()
        rollButton.visibility = View.VISIBLE
        clearButton.visibility = View.VISIBLE
    }


    private fun rollDice() {
        if (selectedDice.isEmpty()) return

        val rollResults = mutableMapOf<Int, List<Int>>()
        var total = 0

        for ((sides, count) in selectedDice) {
            val rolls = List(count) { Random.nextInt(1, sides + 1) }
            rollResults[sides] = rolls
            total += rolls.sum()
        }

        resultText.text = total.toString()
        resultText.visibility = View.VISIBLE

        // Prepare detailed breakdown text for bottom of screen
        val breakdown = rollResults.entries.joinToString("\n") { (sides, rolls) ->
            "${rolls.size}d$sides: ${rolls.joinToString(", ")}"
        }
        detailedRollsText.text = breakdown

        // Log the roll in RollLog
        RollLog.addEntry(total, breakdown)

        // Hide buttons after rolling
        rollButton.visibility = View.INVISIBLE
        clearButton.visibility = View.INVISIBLE
    }

    private fun clearDice() {
        selectedDice.clear()
        detailedRollsText.text = ""
        resultText.text = ""
        resultText.visibility = View.INVISIBLE
        rollButton.visibility = View.VISIBLE
        clearButton.visibility = View.VISIBLE
    }

    private fun updateDetailedRollsText() {
        val breakdown = selectedDice.entries.joinToString("\n") { (sides, count) ->
            "${count}d$sides"
        }
        detailedRollsText.text = breakdown
    }
}
