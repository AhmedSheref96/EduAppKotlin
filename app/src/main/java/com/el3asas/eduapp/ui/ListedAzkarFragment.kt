package com.el3asas.eduapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.el3asas.eduapp.R
import com.el3asas.eduapp.databinding.FragmentListedAzkarListBinding
import com.el3asas.eduapp.models.AzkarEntity
import com.el3asas.eduapp.ui.adapters.MyListedAzkarRecyclerViewAdapter
import com.el3asas.eduapp.ui.adapters.MyListedAzkarRecyclerViewAdapter.ClickListener
import com.el3asas.eduapp.ui.adapters.MyListedAzkarRecyclerViewAdapter.LoveClickListener
import com.el3asas.eduapp.view_models.ListedFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListedAzkarFragment : Fragment(), ClickListener, LoveClickListener {
    private var azkarEntities: List<AzkarEntity>? = null
    private var viewModel: ListedFragmentViewModel? = null
    private lateinit var category: Array<String?>
    private var title: String? = null
    private var binding: FragmentListedAzkarListBinding? = null
    private var adapter: MyListedAzkarRecyclerViewAdapter? = null
    private var fav = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListedAzkarListBinding.inflate(inflater)
        title = ListedAzkarFragmentArgs.fromBundle(arguments).categoryTitle
        category = ListedAzkarFragmentArgs.fromBundle(arguments).categoriesName
        binding!!.title.title = title
        binding!!.title.setNavigationOnClickListener { view: View? ->
            NavHostFragment.findNavController(
                this
            ).navigateUp()
        }
        if (fav) {
            binding!!.title.menu.getItem(0).setIcon(R.drawable.ic_wheart_fill)
        } else {
            binding!!.title.menu.getItem(0).setIcon(R.drawable.ic_wheart)
        }
        viewModel = ViewModelProvider(this).get(ListedFragmentViewModel::class.java)
        binding!!.title.setOnMenuItemClickListener { item: MenuItem ->
            fav = !fav
            if (fav) {
                item.setIcon(R.drawable.ic_wheart_fill)
            } else {
                item.setIcon(R.drawable.ic_wheart)
            }
            handleFav(if (fav) 1 else 2)
            false
        }
        return binding!!.root
    }

    override fun onStart() {
        super.onStart()
        adapter = MyListedAzkarRecyclerViewAdapter(this, this)
        handleFav(if (fav) 1 else if (category.size > 0) 2 else 3)
        binding!!.list.adapter = adapter
    }

    fun handleFav(fav: Int) {
        if (fav == 1) {
            viewModel!!.getAllAzkar(category, true)
                .observe(viewLifecycleOwner) { azkarEntities1: List<AzkarEntity>? ->
                    azkarEntities = azkarEntities1
                    adapter!!.setList(azkarEntities)
                }
        } else if (fav == 2) {
            viewModel!!.getAllAzkar(category)
                .observe(viewLifecycleOwner) { azkarEntities1: List<AzkarEntity>? ->
                    adapter!!.setList(azkarEntities1)
                    azkarEntities = azkarEntities1
                }
        } else {
            viewModel!!.getFavAzkar(true)
                .observe(viewLifecycleOwner) { azkarEntities1: List<AzkarEntity>? ->
                    adapter!!.setList(azkarEntities1)
                    azkarEntities = azkarEntities1
                }
        }
    }

    override fun onClickListener(pos: Int) {
        val directions = ListedAzkarFragmentDirections.actionListedAzkarFragmentToAzkarFragment(
            azkarEntities!!.toTypedArray(), title
        )
        directions.pos = pos
        NavHostFragment.findNavController(this).navigate(directions)
    }

    override fun onChangeStatus(pos: Int, status: Boolean) {
        viewModel!!.updateStatus(status, pos)
    }
}