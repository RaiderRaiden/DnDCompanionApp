package com.example.dndcompanionapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val diceTypes = listOf(4, 6, 8, 10, 12, 20, 100)
    private val diceSelection = mutableMapOf<Int, Int>()
    private val diceButtons = mutableMapOf<Int, Button>()

    private lateinit var summaryText: TextView
    private lateinit var resultText: TextView
    private lateinit var totalText: TextView
    private lateinit var buttonsCenterContainer: LinearLayout
    private lateinit var rollButton: Button
    private lateinit var clearButton: Button

    private fun updateUI() {
        val summary = diceSelection
            .filter { it.value > 0 }
            .entries.joinToString(" + ") { "${it.value}d${it.key}" }

        summaryText.text = summary

        // Show or hide the buttons container depending on if any dice are selected
        buttonsCenterContainer.visibility = if (diceSelection.values.sum() > 0) View.VISIBLE else View.GONE

        // Hide total & result while selecting (but we keep resultText to show previous rolls)
        totalText.visibility = View.GONE
        // Don't clear resultText here, so previous roll stays visible until clear

        // Update die button labels with counts
        for ((sides, button) in diceButtons) {
            val count = diceSelection[sides] ?: 0
            button.text = if (count > 0) "d$sides (x$count)" else "Add d$sides"
        }
    }

    private fun rollAllDice(): Pair<String, Int> {
        val resultBuilder = StringBuilder()
        var total = 0
        for ((sides, count) in diceSelection) {
            if (count > 0) {
                val rolls = List(count) { Random.nextInt(1, sides + 1) }
                val subtotal = rolls.sum()
                resultBuilder.append("${count}d$sides: ${rolls.joinToString(", ")} (Total: $subtotal)\n")
                total += subtotal
            }
        }
        return Pair(resultBuilder.toString().trim(), total)
    }

    private fun clearSelection() {
        diceSelection.keys.forEach { diceSelection[it] = 0 }
        summaryText.text = ""
        resultText.text = ""
        totalText.text = ""
        totalText.visibility = View.GONE
        updateUI()
    }

    private fun setupDieButton(button: Button, sides: Int) {
        diceButtons[sides] = button
        diceSelection[sides] = 0
        button.setOnClickListener {
            diceSelection[sides] = diceSelection[sides]!! + 1
            updateUI()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        summaryText = findViewById(R.id.summaryText)
        resultText = findViewById(R.id.resultText)
        totalText = findViewById(R.id.totalText)
        buttonsCenterContainer = findViewById(R.id.buttonsCenterContainer)
        rollButton = findViewById(R.id.rollButton)
        clearButton = findViewById(R.id.clearButton)

        // Setup die buttons
        setupDieButton(findViewById(R.id.d4Button), 4)
        setupDieButton(findViewById(R.id.d6Button), 6)
        setupDieButton(findViewById(R.id.d8Button), 8)
        setupDieButton(findViewById(R.id.d10Button), 10)
        setupDieButton(findViewById(R.id.d12Button), 12)
        setupDieButton(findViewById(R.id.d20Button), 20)
        setupDieButton(findViewById(R.id.d100Button), 100)

        rollButton.setOnClickListener {
            val (details, total) = rollAllDice()
            resultText.text = details          // Detailed rolls at bottom
            totalText.text = total.toString()  // Big grand total centered
            totalText.visibility = View.VISIBLE
            buttonsCenterContainer.visibility = View.GONE
            // Do NOT clear dice selection here, keep summary visible
        }

        clearButton.setOnClickListener {
            clearSelection()
            buttonsCenterContainer.visibility = View.VISIBLE
        }

        updateUI()
    }
}
