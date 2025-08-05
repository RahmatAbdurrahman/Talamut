package com.android.ecoscan.ui.tracker

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.ecoscan.data.db.WasteDatabase
import com.android.ecoscan.data.repository.WasteRepository
import com.android.ecoscan.databinding.FragmentTrackerBinding
import com.android.ecoscan.ui.adapter.WasteAdapter
import com.android.ecoscan.ui.trash.AddTrashActivity
import com.android.ecoscan.ui.trash.EditTrashActivity
import com.android.ecoscan.ui.viewmodel.WasteViewModel
import com.android.ecoscan.ui.viewmodel.WasteViewModelFactory
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class TrackerFragment : Fragment() {

    private var _binding: FragmentTrackerBinding? = null
    private val binding get() = _binding!!

    private lateinit var pieChart: PieChart
    private lateinit var wasteAdapter: WasteAdapter

    private val viewModel: WasteViewModel by viewModels {
        val dao = WasteDatabase.getDatabase(requireContext()).wasteDao()
        val repository = WasteRepository.getInstance(dao)
        WasteViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackerBinding.inflate(inflater, container, false)
        pieChart = binding.pieChart

        setupPieChart()
        setupRecyclerView()
        observeWasteData()

        binding.btnAddWaste.setOnClickListener {
            val intent = Intent(requireContext(), AddTrashActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun setupPieChart() {
        pieChart.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            setTransparentCircleAlpha(110)
            centerText = "Jenis Sampah"
            setCenterTextSize(18f)
            animateY(1200, Easing.EaseInOutQuad)
            legend.isEnabled = true
        }
    }

    private fun setupRecyclerView() {
        wasteAdapter = WasteAdapter(
            emptyList(),
            onDeleteClick = { waste ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Hapus Data")
                    .setMessage("Yakin ingin menghapus '${waste.name}'?")
                    .setPositiveButton("Hapus") { _, _ ->
                        viewModel.deleteWaste(waste)
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            },
            onItemClick = { waste ->
                val intent = Intent(requireContext(), EditTrashActivity::class.java).apply {
                    putExtra("waste_id", waste.id)
                    putExtra("waste_name", waste.name)
                    putExtra("waste_type", waste.type)
                    putExtra("waste_date", waste.date)
                }
                startActivity(intent)
            }
        )

        binding.wasteRecyclerView.apply {
            adapter = wasteAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeWasteData() {
        viewModel.allWaste.asLiveData().observe(viewLifecycleOwner) { wastes ->
            wasteAdapter.updateList(wastes)

            val typeCount = wastes.groupingBy { it.type }.eachCount()
            val entries = mutableListOf<PieEntry>()
            val colors = mutableListOf<Int>()

            typeCount.forEach { (type, count) ->
                val label = type.trim().lowercase()
                when (label) {
                    "organik" -> {
                        entries.add(PieEntry(count.toFloat(), "Organik"))
                        colors.add(Color.parseColor("#66BB6A"))
                    }
                    "anorganik", "non organik" -> {
                        entries.add(PieEntry(count.toFloat(), "Anorganik"))
                        colors.add(Color.parseColor("#FFA726"))
                    }
                    "b3" -> {
                        entries.add(PieEntry(count.toFloat(), "B3"))
                        colors.add(Color.parseColor("#EF5350"))
                    }
                }
            }


            val dataSet = PieDataSet(entries, "")
            dataSet.colors = colors

            val data = PieData(dataSet).apply {
                setDrawValues(true)
                setValueTextSize(14f)
                setValueTextColor(Color.WHITE)
            }

            pieChart.data = data
            pieChart.invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
