package com.el3asas.eduapp.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.el3asas.eduapp.models.AzkarEntity
import com.el3asas.eduapp.ui.adapters.CatAzkarRVAdapter
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.el3asas.eduapp.databinding.FragmentAzkarBinding

class AzkarFragment : Fragment() {
    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAzkarBinding.inflate(inflater)
        val azkarEntities = AzkarFragmentArgs.fromBundle(arguments).azkarList
        val title = AzkarFragmentArgs.fromBundle(arguments).mainCategoryName
        val pos = AzkarFragmentArgs.fromBundle(arguments).pos
        val catAzkarRVAdapter = CatAzkarRVAdapter()
        catAzkarRVAdapter.setList(azkarEntities)
        assert(azkarEntities != null)
        binding.title.title = title
        binding.title.setNavigationOnClickListener { view: View? ->
            NavHostFragment.findNavController(
                this
            ).navigateUp()
        }
        binding.list.adapter = catAzkarRVAdapter
        binding.list.clipToPadding = false
        binding.list.clipChildren = false
        binding.list.offscreenPageLimit = 3
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page: View, position: Float ->
            val t = 1 - Math.abs(position)
            page.scaleY = .95f + t * .05f
        }
        binding.list.setPageTransformer(transformer)
        binding.list.currentItem = pos
        return binding.root
    }
}