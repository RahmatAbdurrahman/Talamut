package com.android.ecoscan.ui.guide

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.ecoscan.R

class GuideFragment : Fragment() {

    data class GuideItem(val title: String, val iconResId: Int)

    class GuideAdapter(
        private val items: List<GuideItem>,
        private val onClick: (GuideItem) -> Unit
    ) : RecyclerView.Adapter<GuideAdapter.GuideViewHolder>() {

        inner class GuideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val icon: ImageView = itemView.findViewById(R.id.ivWasteIcon)
            val title: TextView = itemView.findViewById(R.id.tvGuideTitle)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_guide, parent, false)
            return GuideViewHolder(view)
        }

        override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
            val item = items[position]
            holder.title.text = item.title
            holder.icon.setImageResource(item.iconResId)
            holder.itemView.setOnClickListener { onClick(item) }
        }

        override fun getItemCount(): Int = items.size
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_guide, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvGuide)

        val guideList = listOf(
            GuideItem("Organik", R.drawable.ic_organik),
            GuideItem("Non Organik", R.drawable.ic_nonorganik),
            GuideItem("B3", R.drawable.ic_b3)
        )

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = GuideAdapter(guideList) { selected ->
                val intent = Intent(requireContext(), GuideDetailActivity::class.java).apply {
                    putExtra("guide_title", selected.title)
                }
                startActivity(intent)
            }
        }

        return view
    }
}
