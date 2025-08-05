package com.android.ecoscan.ui.home


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.ecoscan.MainActivity
import com.android.ecoscan.R
import com.android.ecoscan.adapter.ImageSliderAdapter
import com.android.ecoscan.adapter.NewsAdapter
import com.android.ecoscan.data.api.ApiConfig
import com.android.ecoscan.data.repository.NewsRepository
import com.android.ecoscan.databinding.FragmentHomeBinding
import com.android.ecoscan.ui.guide.GuideFragment
import com.android.ecoscan.ui.scan.ScanFragment
import com.android.ecoscan.ui.tracker.TrackerFragment
import com.android.ecoscan.ui.viewmodel.NewsViewModel
import com.android.ecoscan.ui.viewmodel.NewsViewModelFactory


class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var newsViewModel: NewsViewModel


    private val imageList = listOf(
        R.drawable.banner_1,
        R.drawable.banner_2,
        R.drawable.banner_3
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiService = ApiConfig.getApiService()
        val repository = NewsRepository(apiService)
        val factory = NewsViewModelFactory(repository)

        newsViewModel = ViewModelProvider(this, factory)[NewsViewModel::class.java]
        newsViewModel.fetchNews()


        // image slider
        val imageAdapter = ImageSliderAdapter(imageList)
        binding.imageSliderViewPager.adapter = imageAdapter

        // news list
        val newsAdapter = NewsAdapter()
        binding.recyclerViewNews.apply {
            layoutManager  = LinearLayoutManager(requireContext())
            adapter = newsAdapter

        }

        newsViewModel.newsList.observe(viewLifecycleOwner) { list ->
            newsAdapter.submitList(list)
        }

        newsViewModel.newsList.observe(viewLifecycleOwner) { list ->
            newsAdapter.submitList(list)
        }

        newsViewModel.isError.observe(viewLifecycleOwner) { error ->
        }

        newsViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
        }

        binding.btnGuide.setOnClickListener {
            (activity as? MainActivity)?.let {
                it.navigateToFragment(GuideFragment(), "Panduan")
                it.binding.bottomNavigation.selectedItemId = R.id.nav_guide
            }
        }

        binding.btnScan.setOnClickListener {
            (activity as? MainActivity)?.let {
                it.navigateToFragment(ScanFragment(), "Pindai Sampah")
                it.binding.bottomNavigation.selectedItemId = R.id.nav_scan
            }
        }

        binding.btnTracker.setOnClickListener {
            (activity as? MainActivity)?.let {
                it.navigateToFragment(TrackerFragment(), "Pelnghitung Sampah")
                it.binding.bottomNavigation.selectedItemId = R.id.nav_tracker
            }
        }





    }


}