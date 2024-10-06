package com.example.cryptic

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request

class FirstFragment : Fragment() {

    private val dataAdapter = DataAdapter()
    private val coinList = ArrayList<CrypticData>()

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
        val searchView : SearchView = view.findViewById(R.id.searchView)
        rvList.adapter = dataAdapter
        rvList.layoutManager = LinearLayoutManager(activity)

        btnAdd.setOnClickListener {
            coinList.clear()
            rvList.removeAllViewsInLayout()
            requestData { coins ->
                coins.forEach {
                    coinList.add(it)
                } }
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

    }
    private fun filterList(query : String?) {
        if (query != null) {
            val filteredList = ArrayList<CrypticData>()
            for (i in coinList) {
                if (i.name.lowercase().contains(query)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) { Toast.makeText(activity,"No Data found", Toast.LENGTH_SHORT).show() }
            else { dataAdapter.setFilteredList(filteredList) }

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