package com.example.cryptic

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
            binding.tvName.text = data.name
            binding.tvPrice.text = data.current_price.toString()
            binding.tvSymbol.text = data.symbol
            binding.tvPriceChange24h.text = data.price_change_percentage_24h.toString()
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