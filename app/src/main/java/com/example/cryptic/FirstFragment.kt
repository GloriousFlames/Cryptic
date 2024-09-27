package com.example.cryptic

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request

class FirstFragment : Fragment() {

    private val dataAdapter = DataAdapter()
    val coinDict : MutableMap<String,Float> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvList: RecyclerView = view.findViewById(R.id.rvList)
        val btnAdd: Button = view.findViewById(R.id.btnAdd)

        rvList.adapter = dataAdapter
        rvList.layoutManager = LinearLayoutManager(activity)

        btnAdd.setOnClickListener {
            coinDict.clear()
            rvList.removeAllViewsInLayout()
            requestData { coins ->
                coins.forEach {
                    coinDict[it.name] = it.current_price
                } }
        }
    }

    private fun requestData(callback: (List<CrypticData>) -> Unit) {
        Thread {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("x-cg-demo-api-key", "CG-N17Cm97mgdHGNbPD24Ys1kpP")
                .build()

            val response = client.newCall(request).execute()

            val coins: ArrayList<CrypticData> = jacksonObjectMapper().readValue(response.body!!.string())
            callback(coins)

            Handler(Looper.getMainLooper()).post { coins.forEach { dataAdapter.addData(it) } }

        }.start()
    }
}