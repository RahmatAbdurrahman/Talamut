package com.android.ecoscan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.ecoscan.databinding.ActivityMainBinding
import com.android.ecoscan.ui.guide.GuideFragment
import com.android.ecoscan.ui.home.HomeFragment
import com.android.ecoscan.ui.scan.ScanFragment
import com.android.ecoscan.ui.tracker.TrackerFragment


class MainActivity : AppCompatActivity() {
 lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    navigateToFragment(HomeFragment(), "Halaman Utama")
                    true
                }
                R.id.nav_guide -> {
                    navigateToFragment(GuideFragment(), "Panduan")
                    true
                }
                R.id.nav_scan -> {
                    navigateToFragment(ScanFragment(), "Pindai Sampah")
                    true
                }
                R.id.nav_tracker -> {
                    navigateToFragment(TrackerFragment(), "Penghitung Sampah")
                    true
                }
                else -> false
            }
        }

    }
    fun navigateToFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
        binding.headerText.text = title
    }
}