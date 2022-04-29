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
import android.view.View
import com.el3asas.eduapp.ui.adapters.PraiesAdapter.prayClickListener
import com.el3asas.eduapp.ui.adapters.PraiesAdapter
import androidx.fragment.app.FragmentPagerAdapter
import com.el3asas.eduapp.databinding.FragmentListedAzkarBinding
import com.el3asas.eduapp.ui.adapters.SectionsPagerAdapter

class MyListedAzkarRecyclerViewAdapter(
    private val clickListener: ClickListener,
    private val loveClickListener: LoveClickListener
) : RecyclerView.Adapter<MyListedAzkarRecyclerViewAdapter.ViewHolder>() {
    private var mValues: List<AzkarEntity>? = null
    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: List<AzkarEntity>?) {
        mValues = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentListedAzkarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.fragmentListedAzkarBinding.viewModel = mValues!![position]
        holder.fragmentListedAzkarBinding.root.setOnClickListener { view: View? ->
            clickListener.onClickListener(
                holder.bindingAdapterPosition
            )
        }
        holder.fragmentListedAzkarBinding.likeZ.setOnClickListener { view: View? ->
            mValues!![position].isStatus = holder.fragmentListedAzkarBinding.likeZ.isChecked
            loveClickListener.onChangeStatus(mValues!![position].id, mValues!![position].isStatus)
        }
    }

    override fun getItemCount(): Int {
        return if (mValues != null && mValues!!.size != 0) mValues!!.size else 0
    }

    class ViewHolder(var fragmentListedAzkarBinding: FragmentListedAzkarBinding) :
        RecyclerView.ViewHolder(
            fragmentListedAzkarBinding.root
        )

    interface ClickListener {
        fun onClickListener(pos: Int)
    }

    interface LoveClickListener {
        fun onChangeStatus(pos: Int, status: Boolean)
    }
}