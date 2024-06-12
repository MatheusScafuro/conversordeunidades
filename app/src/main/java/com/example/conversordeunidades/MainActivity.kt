package com.example.conversordeunidades

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.exchangerate-api.com/v4/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(ExchangeRateApi::class.java)

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
            when (conversionType) {
                "Comprimento (Metros para Quilômetros)" -> outputValue.text = convertMetersToKilometers(inputDouble).toString()
                "Temperatura (Celsius para Fahrenheit)" -> outputValue.text = convertCelsiusToFahrenheit(inputDouble).toString()
                "Peso (Quilogramas para Libras)" -> outputValue.text = convertKilogramsToPounds(inputDouble).toString()
                "Moeda (USD para EUR)" -> getExchangeRate("USD", "EUR") { rate -> outputValue.text = (inputDouble * rate).toString() }
                "Moeda (BRL para USD)" -> getExchangeRate("BRL", "USD") { rate -> outputValue.text = (inputDouble * rate).toString() }
                else -> outputValue.text = "0.0"
            }
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

    private fun getExchangeRate(base: String, target: String, callback: (Double) -> Unit) {
        service.getRates(base).enqueue(object : Callback<ExchangeRatesResponse> {
            override fun onResponse(call: Call<ExchangeRatesResponse>, response: Response<ExchangeRatesResponse>) {
                if (response.isSuccessful) {
                    val rate = response.body()?.rates?.get(target)
                    if (rate != null) {
                        callback(rate)
                    } else {
                        Toast.makeText(this@MainActivity, "Erro ao obter taxa de câmbio", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Erro ao obter taxa de câmbio", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ExchangeRatesResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Erro de rede", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
