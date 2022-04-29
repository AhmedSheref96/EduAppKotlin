package com.el3asas.eduapp.ui

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import com.el3asas.eduapp.models.PrayItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.el3asas.eduapp.ui.adapters.PraiesAdapter.prayClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.el3asas.eduapp.R
import com.el3asas.eduapp.ui.adapters.PraiesAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.content.DialogInterface.OnShowListener
import android.content.DialogInterface
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.el3asas.eduapp.databinding.PrayTimeFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetPrayTimes internal constructor(
    private val pref: SharedPreferences,
    list: List<PrayItem>
) : BottomSheetDialogFragment(), prayClickListener {
    private val edit: SharedPreferences.Editor = pref.edit()
    private val prayItems: List<PrayItem> = list
    private val prays =
        arrayOf("azanFajr", "azanShuruq", "azanZohr", "azanAssr", "azanMaghrib", "azanIshaa")
    var isShow = false
        private set

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: PrayTimeFragmentBinding =
            DataBindingUtil.inflate(inflater, R.layout.pray_time_fragment, container, false)
        val adapter = PraiesAdapter()
        adapter.setAdapter(prayItems, pref, this)
        binding.praiesTimes.layoutManager = GridLayoutManager(activity, 2)
        binding.praiesTimes.addItemDecoration(object : ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect[16, 16, 16] = 16
            }
        })
        binding.praiesTimes.adapter = adapter
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dia: DialogInterface ->
            val dialog = dia as BottomSheetDialog
            val bottomSheet =
                dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)!!
            bottomSheet.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.transparent
                )
            )
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(bottomSheet).skipCollapsed = true
            BottomSheetBehavior.from(bottomSheet).setHideable(true)
        }
        bottomSheetDialog.setCancelable(true)
        return bottomSheetDialog
    }

    override fun clickListener(pos: Int, isChecked: Boolean) {
        if (isChecked) {
            edit.putBoolean(prays[pos], true)
            edit.apply()
        } else {
            edit.putBoolean(prays[pos], false)
            edit.apply()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isShow = true
        Log.d("", "onAttach: --------")
    }

    override fun onDetach() {
        super.onDetach()
        isShow = false
        Log.d("", "onDetach: ---------")
    }

}