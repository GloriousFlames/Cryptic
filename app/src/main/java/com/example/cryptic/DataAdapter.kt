package com.example.cryptic

import android.graphics.Color
import android.icu.text.DecimalFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptic.databinding.DataItemBinding

class DataAdapter: RecyclerView.Adapter<DataAdapter.DataHolder>() {
    val curList = ArrayList<CrypticData>()
    class DataHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = DataItemBinding.bind(item)
        fun bind(data: CrypticData) {
            val decimalFormat = DecimalFormat("#.###")
            binding.tvName.text = data.name
            binding.tvPrice.text = "$${decimalFormat.format(data.current_price)}"
            binding.tvSymbol.text = data.symbol.uppercase()
            if (data.price_change_percentage_24h >= 0) {
                binding.tvPriceChange24h.setTextColor(Color.parseColor("#25FF00"))
                binding.tvPriceChange24h.text = "⬉${decimalFormat.format(data.price_change_percentage_24h)}%"
            }
            else {
                binding.tvPriceChange24h.setTextColor(Color.parseColor("#FF0000"))
                binding.tvPriceChange24h.text = "⬋${decimalFormat.format(data.price_change_percentage_24h)}%"
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

    fun addData(data: CrypticData) {
        curList.add(data)
        notifyDataSetChanged()
    }
}