package com.example.cryptic

import android.icu.number.Precision.currency
import android.icu.text.DecimalFormat
import android.icu.util.Currency
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request
import org.w3c.dom.Text
import java.text.NumberFormat
import java.util.Locale


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
        var coins = CurrencyObject(mapOf(),CurrencyMeta(""))
        var currency = "USD"
        var leftSymbol : TextView = view.findViewById(R.id.leftSymbol)
        var rightSymbol : TextView = view.findViewById(R.id.rightSymbol)

        balance = view.findViewById(R.id.balance)
        balance.setOnClickListener {
            requestData { currency -> coins = currency }
            val popupMenu = PopupMenu(activity, balance, Gravity.TOP)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.show()
            val decimalFormat = DecimalFormat("#.##")
            popupMenu.setOnMenuItemClickListener { item : MenuItem ->
                when (item.itemId) {
                    R.id.menuUSD -> {
                        if (currency != "USD") {
                            leftSymbol.text = "$"
                            rightSymbol.text = ""
                            for (cur in coins.data.keys) {
                                if (cur == currency) {
                                    balance.text = decimalFormat.format(balance.text.toString().toFloat() / coins.data[cur]!!.value)
                                    currency = "USD"
                                }
                            }
                        }
                    }
                    R.id.menuRUB -> {
                        if (currency != "RUB") {
                            leftSymbol.text = ""
                            rightSymbol.text = "RUB"
                            for (cur in coins.data.keys) {
                                if (cur == currency) {
                                    balance.text = decimalFormat.format(balance.text.toString().toFloat() / coins.data[cur]!!.value)
                                }
                            }
                            for (cur in coins.data.keys) {
                                if (cur == "RUB") {
                                    balance.text = decimalFormat.format(balance.text.toString().toFloat() * coins.data[cur]!!.value)
                                    currency = "RUB"
                                }
                            }
                        }
                    }
                    R.id.menuUAH -> {
                        if (currency != "UAH") {
                            leftSymbol.text = ""
                            rightSymbol.text = "UAH"
                            for (cur in coins.data.keys) {
                                if (cur == currency) {
                                    balance.text = decimalFormat.format(balance.text.toString().toFloat() / coins.data[cur]!!.value)
                                }
                            }
                            for (cur in coins.data.keys) {
                                if (cur == "UAH") {
                                    balance.text = decimalFormat.format(balance.text.toString().toFloat() * coins.data[cur]!!.value)
                                    currency = "UAH"
                                }
                            }
                        }
                    }
                    R.id.menuEUR -> {
                        if (currency != "EUR") {
                            leftSymbol.text = "€"
                            rightSymbol.text = ""
                            for (cur in coins.data.keys) {
                                if (cur == currency) {
                                    balance.text = decimalFormat.format(balance.text.toString().toFloat() / coins.data[cur]!!.value)
                                }
                            }
                            for (cur in coins.data.keys) {
                                if (cur == "EUR") {
                                    balance.text = decimalFormat.format(balance.text.toString().toFloat() * coins.data[cur]!!.value)
                                    currency = "EUR"
                                }
                            }
                        }
                    }
                    R.id.menuBYN -> {
                        if (currency != "BYN") {
                            leftSymbol.text = ""
                            rightSymbol.text = "BYN"
                            for (cur in coins.data.keys) {
                                if (cur == currency) {
                                    balance.text = decimalFormat.format(balance.text.toString().toFloat() / coins.data[cur]!!.value)
                                }
                            }
                            for (cur in coins.data.keys) {
                                if (cur == "BYN") {
                                    balance.text = decimalFormat.format(balance.text.toString().toFloat() * coins.data[cur]!!.value)
                                    currency = "BYN"
                                }
                            }
                        }
                    }
                }
                Log.i("Currency", "$coins")
                true
            }
        }

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

    private fun requestData(callback: (CurrencyObject) -> Unit) {
        Thread {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.currencyapi.com/v3/latest?apikey=cur_live_L9xydszOXu9R6YBJ0iTaTWA3jALHzHg8bi3eOAUJ&currencies=USD,RUB,UAH,EUR,BYN")
                .get()
                .build()

            val response = client.newCall(request).execute()

            val currency: CurrencyObject = jacksonObjectMapper().readValue(response.body!!.string())

            Handler(Looper.getMainLooper()).post { callback(currency) }

        }.start()
    }
}