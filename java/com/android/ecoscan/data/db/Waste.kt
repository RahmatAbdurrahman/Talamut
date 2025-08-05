package com.android.ecoscan.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "waste_table")
data class Waste(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,              // Nama sampah
    val type: String,              // Tipe sampah
    val quantity: Int,             // Jumlah
    val date: String              // Tanggal dibuang (format bebas misal: "3 Agustus 2025")
)
