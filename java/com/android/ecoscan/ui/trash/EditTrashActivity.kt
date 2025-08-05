package com.android.ecoscan.ui.trash

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.ecoscan.R
import com.android.ecoscan.data.db.Waste
import com.android.ecoscan.data.db.WasteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditTrashActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etType: AutoCompleteTextView
    private lateinit var etDate: EditText
    private lateinit var etQuantity: EditText
    private lateinit var btnUpdate: Button

    private var wasteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_trash)

        val toolbar = findViewById<Toolbar>(R.id.toolbarEdit)
        setSupportActionBar(toolbar)

        toolbar.setTitleTextColor(resources.getColor(android.R.color.white, theme))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()  // atau gunakan finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etName = findViewById(R.id.etName)
        etType = findViewById(R.id.etType)
        etDate = findViewById(R.id.etDate)
        etQuantity = findViewById(R.id.etQuantity)
        btnUpdate = findViewById(R.id.btnUpdate)

        // Set default options for type dropdown
        val types = listOf("Organik", "Anorganik", "B3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, types)
        etType.setAdapter(adapter)

        // Handle tanggal picker
        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    val sdf = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID"))
                    calendar.set(year, month, dayOfMonth)
                    etDate.setText(sdf.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Get data dari Intent
        wasteId = intent.getIntExtra("waste_id", -1)
        val name = intent.getStringExtra("waste_name") ?: ""
        val type = intent.getStringExtra("waste_type") ?: ""
        val date = intent.getStringExtra("waste_date") ?: ""
        val quantity = intent.getIntExtra("waste_quantity", 0)

        etName.setText(name)
        etType.setText(type, false)
        etDate.setText(date)
        etQuantity.setText(quantity.toString())

        btnUpdate.setOnClickListener {
            val updatedWaste = Waste(
                id = wasteId,
                name = etName.text.toString(),
                type = etType.text.toString(),
                date = etDate.text.toString(),
                quantity = etQuantity.text.toString().toIntOrNull() ?: 0
            )

            CoroutineScope(Dispatchers.IO).launch {
                WasteDatabase.getDatabase(this@EditTrashActivity).wasteDao().updateWaste(updatedWaste)
                finish() // kembali ke sebelumnya
            }
        }
    }
}
