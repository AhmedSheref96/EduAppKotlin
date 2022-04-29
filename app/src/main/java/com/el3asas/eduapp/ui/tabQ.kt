package com.el3asas.eduapp.ui

import com.el3asas.eduapp.ui.adapters.MyAdapterQ
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.el3asas.eduapp.ui.tabQ
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.el3asas.eduapp.R
import androidx.recyclerview.widget.GridLayoutManager
import com.el3asas.eduapp.databinding.QFragmentBinding
import com.el3asas.eduapp.models.Entity
import com.el3asas.eduapp.view_models.PageViewModel

class tabQ : Fragment(), MyAdapterQ.clickListener {
    private var entities: List<Entity>? = null
    private lateinit var aFragmentBinding: QFragmentBinding
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = MyAdapterQ(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        aFragmentBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.q_fragment, viewGroup, false)

        //mSharedModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        aFragmentBinding.items.layoutManager = GridLayoutManager(activity, 1)
        PageViewModel.qData!!.observe(viewLifecycleOwner) { entities: List<Entity>? ->
            this.entities = entities
            myAdapter!!.setList(this.entities)
            myAdapter!!.notifyDataSetChanged()
            observeSharedData()
        }
        aFragmentBinding.items.adapter = myAdapter
        return aFragmentBinding.getRoot()
    }

    private fun observeSharedData() {
        /*  previousPostFragment.sharedViewModel.searchQueryLiveData.observe(getViewLifecycleOwner(), s -> {myAdapter.getFilter().filter(s);
            Log.d("", "observeSharedData: qqqqqqqq  " + s);
        });*/
    }

    override fun onClickItemListerer(position: Int) {
        Log.d(
            "TAGggggggggg",
            "onClickItemListerer: " + position + "------------" + entities!![position].quot
        )
    }

    companion object {
        //private SharedViewModel mSharedModel;
        private var myAdapter: MyAdapterQ? = null
    }
}