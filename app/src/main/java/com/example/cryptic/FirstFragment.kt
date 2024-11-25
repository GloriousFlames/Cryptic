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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.notify

class FirstFragment : Fragment() {

    private val dataAdapterFirst = DataAdapterFirst()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedList = ViewModelProvider(requireActivity()).get(ShareData::class.java)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewFirst)
        val btnCurrencyAdd: Button = view.findViewById(R.id.btnCurrencyUpdate)
        val searchView : SearchView = view.findViewById(R.id.searchView)
        recyclerView.adapter = dataAdapterFirst
        recyclerView.layoutManager = LinearLayoutManager(activity)

        btnCurrencyAdd.setOnClickListener {
            dataAdapterFirst.curList.clear()
            recyclerView.removeAllViewsInLayout()
            dataAdapterFirst.notifyDataSetChanged()
            requestData()
            sharedList.setData(dataAdapterFirst.curList)
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
            for (i in dataAdapterFirst.curList) {
                if (i.name.lowercase().contains(query)) {
                    filteredList.add(i)
                }
            }
            if (filteredList.isEmpty()) {
                Toast.makeText(activity,"No Data found", Toast.LENGTH_LONG).show()
            }
            dataAdapterFirst.setFilteredList(filteredList)

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

            val coins: ArrayList<CrypticData> = jacksonObjectMapper().readValue(response.body!!.string())

            Handler(Looper.getMainLooper()).post { coins.forEach { dataAdapterFirst.addData(it) } }

        }.start()
    }
}