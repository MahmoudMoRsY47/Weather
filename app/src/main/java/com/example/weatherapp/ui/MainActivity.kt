package com.example.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.weatherapp.data.WeatherInfo
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.util.Constent
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        makeRequestUsingOkhttp("cairo")

        binding.weather.setOnClickListener {
            makeRequestUsingOkhttp(binding.city.text.toString())
        }
    }


    private fun makeRequestUsingOkhttp(name: String) {
        val request = Request.Builder().url("https://api.openweathermap.org/data/2.5/weather?q=$name&appid=${Constent.KEY}").build()


        client.newCall(request = request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("mahmoud", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let {
                    val result = Gson().fromJson(it, WeatherInfo::class.java)
                    runOnUiThread {
                        binding.cityName.text=name
                        binding.temp.text = "${result.main.temp.toInt()-273} °C"
                }
                }
            }
        })
    }
}