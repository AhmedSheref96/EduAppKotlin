package com.el3asas.eduapp.ui.adapters

import com.el3asas.eduapp.view_models.PostsViewModel.update
import androidx.recyclerview.widget.RecyclerView
import com.el3asas.eduapp.ui.adapters.CatAzkarRVAdapter.CatAzkarRecyViewHolder
import com.el3asas.eduapp.models.AzkarEntity
import io.reactivex.rxjava3.disposables.Disposable
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import com.el3asas.eduapp.R
import android.annotation.SuppressLint
import com.el3asas.eduapp.ui.adapters.CatAzkarRVAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import com.el3asas.eduapp.ui.adapters.MainAzkarAdapter.CardListener
import com.el3asas.eduapp.models.MainAzkarItem
import com.el3asas.eduapp.ui.adapters.MainAzkarAdapter.MainAzkarAdapterViewHolder
import android.widget.TextView
import com.el3asas.eduapp.ui.adapters.MainAzkarAdapter
import com.el3asas.eduapp.ui.adapters.MyAdapterA
import android.widget.Filter.FilterResults
import com.el3asas.eduapp.ui.adapters.MyAdapterH
import com.el3asas.eduapp.ui.adapters.MyAdapterQ
import com.el3asas.eduapp.ui.adapters.MyListedAzkarRecyclerViewAdapter.ClickListener
import com.el3asas.eduapp.ui.adapters.MyListedAzkarRecyclerViewAdapter.LoveClickListener
import com.el3asas.eduapp.view_models.PostsViewModel
import com.el3asas.eduapp.ui.adapters.PostsAdapter.luklyViewHolder
import android.widget.CompoundButton
import com.el3asas.eduapp.ui.adapters.PostsAdapter
import com.el3asas.eduapp.models.PrayItem
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.el3asas.eduapp.ui.adapters.PraiesAdapter.prayClickListener
import com.el3asas.eduapp.ui.adapters.PraiesAdapter
import androidx.fragment.app.FragmentPagerAdapter
import com.el3asas.eduapp.ui.adapters.SectionsPagerAdapter
import java.util.ArrayList

class SectionsPagerAdapter(private val mContext: Context, fragmentManager: FragmentManager?) :
    FragmentPagerAdapter(
        fragmentManager!!, 1
    ) {
    private val fragmentList: MutableList<Fragment?> = ArrayList<Any?>()

    // androidx.fragment.app.FragmentPagerAdapter
    override fun getItem(i: Int): Fragment {
        return fragmentList[i]!!
    }

    // androidx.viewpager.widget.PagerAdapter
    override fun getPageTitle(i: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[i])
    }

    // androidx.viewpager.widget.PagerAdapter
    override fun getCount(): Int {
        return fragmentList.size
    }

    fun addFragment(fragment: Fragment?) {
        fragmentList.add(fragment)
    }

    companion object {
        private val TAB_TITLES = intArrayOf(R.string.ayat, R.string.hades, R.string.aqtbas)
    }
}