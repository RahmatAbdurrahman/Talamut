package com.android.ecoscan.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.ecoscan.R
import com.android.ecoscan.data.db.Waste
import java.text.SimpleDateFormat
import java.util.Locale

class WasteAdapter(
    private var list: List<Waste>,
    private val onDeleteClick: (Waste) -> Unit,
    private val onItemClick: (Waste) -> Unit
) : RecyclerView.Adapter<WasteAdapter.WasteViewHolder>() {

    inner class WasteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvWasteName: TextView = itemView.findViewById(R.id.tvWasteName)
        val tvWasteType: TextView = itemView.findViewById(R.id.tvWasteType)
        val tvWasteDate: TextView = itemView.findViewById(R.id.tvWasteDate)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WasteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.waste_item, parent, false)
        return WasteViewHolder(view)
    }

    override fun onBindViewHolder(holder: WasteViewHolder, position: Int) {
        val waste = list[position]
        holder.tvWasteName.text = waste.name
        holder.tvWasteType.text = waste.type

        val inputFormat = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))

        val formattedDate = try {
            val parsedDate = inputFormat.parse(waste.date)
            outputFormat.format(parsedDate!!)
        } catch (e: Exception) {
            waste.date
        }

        holder.tvWasteDate.text = formattedDate

        holder.btnDelete.setOnClickListener {
            onDeleteClick(waste)
        }

        holder.itemView.setOnClickListener {
            onItemClick(waste)
        }
    }

    override fun getItemCount(): Int = list.size

    fun updateList(newList: List<Waste>) {
        list = newList
        notifyDataSetChanged()
    }
}
