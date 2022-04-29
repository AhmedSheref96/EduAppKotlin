package com.el3asas.eduapp.ui

import com.el3asas.eduapp.ui.adapters.MyAdapterH
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.el3asas.eduapp.ui.tabH
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.el3asas.eduapp.R
import androidx.recyclerview.widget.GridLayoutManager
import com.el3asas.eduapp.databinding.HFragmentBinding
import com.el3asas.eduapp.models.Entity
import com.el3asas.eduapp.view_models.PageViewModel

class tabH : Fragment(), MyAdapterH.clickListener {
    private var entities: List<Entity>? = null
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = MyAdapterH(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        val aFragmentBinding: HFragmentBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.h_fragment, viewGroup, false)

        //mSharedModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        aFragmentBinding.items.layoutManager = GridLayoutManager(activity, 1)
        PageViewModel.hData!!.observe(viewLifecycleOwner) { entities: List<Entity>? ->
            this.entities = entities
            myAdapter!!.setList(this.entities)
            myAdapter!!.notifyDataSetChanged()
            observeSharedData()
        }
        aFragmentBinding.items.adapter = myAdapter
        return aFragmentBinding.root
    }

    private fun observeSharedData() {
        /*previousPostFragment.sharedViewModel.searchQueryLiveData.observe(getViewLifecycleOwner(), s -> {myAdapter.getFilter().filter(s);
            Log.d("", "observeSharedData: hhhhhhhhh " + s);
        });*/
    }

    override fun onClickItemListerer(position: Int) {
        Log.d("TAGggggggggg", "onClickItemListerer: $position------------")
    }

    companion object {
        //private SharedViewModel mSharedModel;
        private var myAdapter: MyAdapterH? = null
    }
}