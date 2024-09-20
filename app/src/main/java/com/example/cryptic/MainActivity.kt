package com.example.cryptic

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptic.databinding.ActivityMainBinding
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = DataAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.adapter = adapter
        binding.buttonAdd.setOnClickListener {
            binding.rvList.removeAllViewsInLayout()
            requestData()
        }
        }

    private fun requestData() {
        Thread {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-cg-demo-api-key", "CG-N17Cm97mgdHGNbPD24Ys1kpP")
                .build()

            val response = client.newCall(request).execute()

            val coins: List<CrypticData> = jacksonObjectMapper().readValue(response.body!!.string())
            runOnUiThread { run { coins.forEach { adapter.addData(it) }}}
        }.start()
    }
}