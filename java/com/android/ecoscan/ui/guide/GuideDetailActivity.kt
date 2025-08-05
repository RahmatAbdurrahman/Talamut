package com.android.ecoscan.ui.guide

import android.os.Bundle
import android.text.Html
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.ecoscan.R

class GuideDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guide_detail)

        val toolbar = findViewById<Toolbar>(R.id.toolbarGuide)
        setSupportActionBar(toolbar)

        toolbar.setTitleTextColor(resources.getColor(android.R.color.white, theme))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()  // atau gunakan finish()
        }

        val title = intent.getStringExtra("guide_title") ?: "Panduan"
        val tvTitle = findViewById<TextView>(R.id.tvGuideDetailTitle)
        val tvContent = findViewById<TextView>(R.id.tvGuideDetailContent)

        tvTitle.text = title
        tvContent.text = when (title) {
            "Organik" -> getString(R.string.guide_organik)
            "Non Organik" -> getString(R.string.guide_nonorganik)
            "B3" -> getString(R.string.guide_b3)
            else -> "Tidak ada panduan tersedia."
        }

        tvContent.text = Html.fromHtml(
            when (title) {
                "Organik" -> getString(R.string.guide_organik)
                "Non Organik" -> getString(R.string.guide_nonorganik)
                "B3" -> getString(R.string.guide_b3)
                else -> "Tidak ada panduan tersedia."
            },
            Html.FROM_HTML_MODE_LEGACY
        )
    }

}
