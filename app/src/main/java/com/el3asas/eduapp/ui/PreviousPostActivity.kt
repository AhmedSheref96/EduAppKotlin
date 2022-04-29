package com.el3asas.eduapp.ui

import dagger.hilt.android.AndroidEntryPoint
import androidx.appcompat.app.AppCompatActivity
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.el3asas.eduapp.db.DataBase
import androidx.databinding.DataBindingUtil
import com.el3asas.eduapp.R
import com.el3asas.eduapp.ui.adapters.SectionsPagerAdapter
import com.el3asas.eduapp.view_models.PageViewModel
import androidx.lifecycle.ViewModelProvider
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.el3asas.eduapp.databinding.FragmentPrviousPostBinding

@AndroidEntryPoint
class PreviousPostActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    @SuppressLint("StaticFieldLeak")
    private lateinit var binding: FragmentPrviousPostBinding
    private var indicatorWidth = 0
    private val tabq = tabQ()
    private val taba = tabA()
    private val tabh = tabH()
    override fun onQueryTextSubmit(str: String): Boolean {
        return false
    }

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        DataBase.getInstance(this)
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_prvious_post)
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        //sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        binding.lifecycleOwner = this
        binding.searchView.setOnQueryTextListener(this)
        binding.searchView.isSubmitButtonEnabled = false

        //public SharedViewModel sharedViewModel;
        val pageViewModel = ViewModelProvider(this).get(
            PageViewModel::class.java
        )
        pageViewModel.allQ
        pageViewModel.allH
        pageViewModel.allA
        sectionsPagerAdapter!!.addFragment(tabq)
        sectionsPagerAdapter!!.addFragment(tabh)
        sectionsPagerAdapter!!.addFragment(taba)
        binding.viewPager22.adapter = sectionsPagerAdapter
        binding.tab.setupWithViewPager(binding.viewPager22)
        binding.tab.post {
            indicatorWidth = binding.tab.width / binding.tab.tabCount
            val indicatorParams = binding.indicator.layoutParams as FrameLayout.LayoutParams
            indicatorParams.width = indicatorWidth
            binding.indicator.layoutParams = indicatorParams
        }
        binding.viewPager22.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(i: Int, positionOffset: Float, positionOffsetPx: Int) {
                val params = binding.indicator.layoutParams as FrameLayout.LayoutParams
                val translationOffset = (positionOffset + i) * indicatorWidth
                params.leftMargin = translationOffset.toInt()
                binding.indicator.layoutParams = params
            }

            override fun onPageSelected(i: Int) {}
            override fun onPageScrollStateChanged(i: Int) {}
        })
    }

    override fun onQueryTextChange(str: String): Boolean {
        Log.d("", "onQueryTextChange: +++++++++$str")
        //sharedViewModel.setData(str);
        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var sectionsPagerAdapter: SectionsPagerAdapter? = null
    }
}