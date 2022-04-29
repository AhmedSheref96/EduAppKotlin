package com.el3asas.eduapp.ui

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.icu.util.IslamicCalendar
import android.os.Build
import android.util.Log
import android.view.View
import com.el3asas.eduapp.databinding.FragmentIslamicCalenderBinding
import com.el3asas.eduapp.ui.IslamicCalenderFragment

class IslamicCalenderFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentIslamicCalenderBinding.inflate(inflater)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val islamicCalendar = IslamicCalendar()
            getMessage(islamicCalendar[IslamicCalendar.DATE].toString() + "")
            getMessage(islamicCalendar[IslamicCalendar.MONTH].toString() + "")
            getMessage(islamicCalendar[IslamicCalendar.YEAR].toString() + "")
        }
        return binding.root
    }

    private fun getMessage(s: String) {
        Log.d(TAG, "getMessage: ++++++++++++$s")
    }

    companion object {
        private const val TAG = "Cannot invoke method length() on null object"
    }
}