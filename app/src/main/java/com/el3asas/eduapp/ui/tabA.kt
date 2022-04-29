package com.el3asas.eduapp.ui

import com.el3asas.eduapp.ui.adapters.MyAdapterA
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import com.el3asas.eduapp.ui.tabA
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.el3asas.eduapp.R
import androidx.recyclerview.widget.GridLayoutManager
import com.el3asas.eduapp.databinding.AFragmentBinding
import com.el3asas.eduapp.models.Entity
import com.el3asas.eduapp.view_models.PageViewModel

class tabA : Fragment(), MyAdapterA.clickListener {
    private var entities: List<Entity>? = null
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myAdapter = MyAdapterA(this)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        val aFragmentBinding: AFragmentBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.a_fragment, viewGroup, false)
        //mSharedModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        aFragmentBinding.items.layoutManager = GridLayoutManager(activity, 1)
        PageViewModel.aData!!.observe(viewLifecycleOwner) { entities: List<Entity>? ->
            this.entities = entities
            myAdapter!!.setList(this.entities)
            myAdapter!!.notifyDataSetChanged()
            observeSharedData()
        }
        aFragmentBinding.items.adapter = myAdapter
        return aFragmentBinding.root
    }

    private fun observeSharedData() {
        /*  previousPostFragment.sharedViewModel.searchQueryLiveData.observe(getViewLifecycleOwner(), s -> {myAdapter.getFilter().filter(s);
            Log.d("", "observeSharedData: aaaaaaaaa" + s);
        });*/
    }

    override fun onClickItemListerer(position: Int) {
        Log.d(
            "TAGggggggggg",
            "onClickItemListerer: " + position + "------------" + entities!![position].quot
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("TAG", "onDestroy: +++++++++++++++++++")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TAG", "onDestroyView: ++++++++++++++++")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("TAG", "onDetach: ++++++++++++++++++")
    }

    companion object {
        //private SharedViewModel mSharedModel;
        private var myAdapter: MyAdapterA? = null
    }
}