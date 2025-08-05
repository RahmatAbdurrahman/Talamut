package com.android.ecoscan.ui.trash

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.android.ecoscan.R
import com.android.ecoscan.data.db.Waste
import com.android.ecoscan.data.db.WasteDatabase
import com.android.ecoscan.databinding.ActivityAddTrashBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTrashActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTrashBinding

    private val wasteTypes = listOf("Organik", "Non Organik", "B3")
    private val quantityOptions = (1..20).map { it.toString() }
    private val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTrashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbarAdd)
        setSupportActionBar(toolbar)

        toolbar.setTitleTextColor(resources.getColor(android.R.color.white, theme))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()  // atau gunakan finish()
        }

        // Setup hanya untuk dropdown (type & quantity)
        setupDropdown(binding.etType, wasteTypes)
        setupDropdown(binding.etQuantity, quantityOptions)

        val today = Calendar.getInstance()
        binding.etDate.setText(dateFormat.format(today.time))

        binding.etDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    cal.set(year, month, dayOfMonth)
                    binding.etDate.setText(dateFormat.format(cal.time))
                },
                today.get(Calendar.YEAR),
                today.get(Calendar.MONTH),
                today.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnSave.setOnClickListener {
            val name = binding.etName.text.toString().ifBlank { "Lainnya" }
            val type = binding.etType.text.toString()
            val quantity = binding.etQuantity.text.toString().toIntOrNull()
            val date = binding.etDate.text.toString()

            if (type.isBlank() || quantity == null || date.isBlank()) {
                Toast.makeText(this, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val waste = Waste(name = name, type = type, quantity = quantity, date = date)

            lifecycleScope.launch(Dispatchers.IO) {
                WasteDatabase.getDatabase(this@AddTrashActivity)
                    .wasteDao()
                    .insertWaste(waste)

                launch(Dispatchers.Main) {
                    Toast.makeText(this@AddTrashActivity, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }


    private fun setupDropdown(view: AutoCompleteTextView, items: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        view.setAdapter(adapter)
        view.inputType = EditorInfo.TYPE_NULL
        view.setOnClickListener { view.showDropDown() }
    }
}
