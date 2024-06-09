package com.example.conversordeunidades

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner = findViewById<Spinner>(R.id.spinnerConversionType)
        val inputValue = findViewById<EditText>(R.id.inputValue)
        val convertButton = findViewById<Button>(R.id.convertButton)
        val outputValue = findViewById<TextView>(R.id.outputValue)

        ArrayAdapter.createFromResource(
            this,
            R.array.conversion_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        convertButton.setOnClickListener {
            val input = inputValue.text.toString()

            if (input.isEmpty()) {
                Toast.makeText(this, "Por favor, insira um valor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val inputDouble = input.toDoubleOrNull()
            if (inputDouble == null) {
                Toast.makeText(this, "Por favor, insira um número válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val conversionType = spinner.selectedItem.toString()
            val result = when (conversionType) {
                "Comprimento (Metros para Quilômetros)" -> convertMetersToKilometers(inputDouble)
                "Temperatura (Celsius para Fahrenheit)" -> convertCelsiusToFahrenheit(inputDouble)
                "Peso (Quilogramas para Libras)" -> convertKilogramsToPounds(inputDouble)
                else -> 0.0
            }

            outputValue.text = result.toString()
        }
    }

    private fun convertMetersToKilometers(meters: Double): Double {
        return meters / 1000
    }

    private fun convertCelsiusToFahrenheit(celsius: Double): Double {
        return (celsius * 9 / 5) + 32
    }

    private fun convertKilogramsToPounds(kilograms: Double): Double {
        return kilograms * 2.20462
    }
}
