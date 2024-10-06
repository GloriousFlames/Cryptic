package com.example.cryptic

import android.graphics.Color
import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DataAdapter: RecyclerView.Adapter<DataAdapter.DataHolder>() {

    var curList = ArrayList<CrypticData>()

    class DataHolder(item: View): RecyclerView.ViewHolder(item) {

        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val tvSymbol: TextView = itemView.findViewById(R.id.tvSymbol)
        private val tvPriceChange24h: TextView = itemView.findViewById(R.id.tvPriceChange24h)

        fun bind(data: CrypticData) {
            val decimalFormat = DecimalFormat("#.###")
            tvName.text = data.name
            tvPrice.text = "$${decimalFormat.format(data.current_price)}"
            tvSymbol.text = data.symbol.uppercase()
            if (data.price_change_percentage_24h >= 0) {
                tvPriceChange24h.setTextColor(Color.parseColor("#25FF00"))
                tvPriceChange24h.text = "⬉${decimalFormat.format(data.price_change_percentage_24h)}%"
            }
            else {
                tvPriceChange24h.setTextColor(Color.parseColor("#FF0000"))
                tvPriceChange24h.text = "⬋${decimalFormat.format(data.price_change_percentage_24h)}%"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.data_item, parent, false)
        return DataHolder(view)
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {
        holder.bind(curList[position])
    }

    override fun getItemCount(): Int {
        return curList.size
    }
    fun setFilteredList(list : ArrayList<CrypticData>) {
        curList = list
        notifyDataSetChanged()
    }
    fun addData(data: CrypticData) {
        curList.add(data)
        notifyItemInserted(curList.size - 1)
    }
}