package com.example.cryptic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class SecondFragment : Fragment(), DialogFrag.DialogListener {

    private val dataAdapterSecond = DataAdapterSecond()
    private lateinit var sharedData : ShareData
    private lateinit var balance : TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnInventoryAdd : Button = view.findViewById(R.id.btnInventoryAdd)
        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerViewSecond)
        balance = view.findViewById(R.id.balance)
        recyclerView.adapter = dataAdapterSecond
        recyclerView.layoutManager = LinearLayoutManager(activity)
        updateBalance()

        btnInventoryAdd.setOnClickListener {
            sharedData = ViewModelProvider(requireActivity()).get(ShareData::class.java)
            DialogFrag().show(childFragmentManager, null)
        }
    }

    override fun moveData(name: String, count: Float) {
        for (data in sharedData.sharedList) {
            if (data.name==name) {
                dataAdapterSecond.addData(data,count)
                updateBalance()
                break
            }
        }
    }

    private fun updateBalance() {
        var curBalance = 0f
        for (data in dataAdapterSecond.curList) {
            curBalance += data.current_price*data.count
        }
        balance.text = curBalance.toString()
    }
}