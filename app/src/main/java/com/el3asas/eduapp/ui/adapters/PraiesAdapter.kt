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
import com.el3asas.eduapp.ui.adapters.PraiesAdapter.prayClickListener
import com.el3asas.eduapp.ui.adapters.PraiesAdapter
import androidx.fragment.app.FragmentPagerAdapter
import com.el3asas.eduapp.databinding.PrayTimeItemBinding
import com.el3asas.eduapp.ui.adapters.SectionsPagerAdapter
import java.text.SimpleDateFormat
import java.util.*

class PraiesAdapter : RecyclerView.Adapter<PraiesAdapter.MyViewHolder>() {
    private var prayTimes: List<PrayItem>? = null
    private var pref: SharedPreferences? = null
    private var s: SimpleDateFormat? = null
    private val prays =
        arrayOf("azanFajr", "azanShuruq", "azanZohr", "azanAssr", "azanMaghrib", "azanIshaa")

    fun setAdapter(list: List<PrayItem>?, pref: SharedPreferences?, listener: prayClickListener?) {
        prayTimes = ArrayList(list)
        this.pref = pref
        Companion.listener = listener
        s = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: PrayTimeItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.pray_time_item,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.listItemBinding.name.text = prayTimes!![position].name
        holder.listItemBinding.time.text = setTextSallahTimes(prayTimes!![position].calendar)
        when (position) {
            0 -> {
                holder.listItemBinding.box.isChecked = pref!!.getBoolean(prays[0], true)
                holder.listItemBinding.img.setImageResource(R.drawable.ic_fajr)
            }
            1 -> {
                holder.listItemBinding.box.isChecked = pref!!.getBoolean(prays[1], true)
                holder.listItemBinding.img.setImageResource(R.drawable.ic_shrouq)
            }
            2 -> {
                holder.listItemBinding.box.isChecked = pref!!.getBoolean(prays[2], true)
                holder.listItemBinding.img.setImageResource(R.drawable.ic_zohr)
            }
            3 -> {
                holder.listItemBinding.box.isChecked = pref!!.getBoolean(prays[3], true)
                holder.listItemBinding.img.setImageResource(R.drawable.ic_assr)
            }
            4 -> {
                holder.listItemBinding.box.isChecked = pref!!.getBoolean(prays[4], true)
                holder.listItemBinding.img.setImageResource(R.drawable.ic_maghrib)
            }
            5 -> {
                holder.listItemBinding.box.isChecked = pref!!.getBoolean(prays[5], true)
                holder.listItemBinding.img.setImageResource(R.drawable.ic_ishaa)
            }
        }
    }

    override fun getItemCount(): Int {
        return prayTimes!!.size
    }

    class MyViewHolder(var listItemBinding: PrayTimeItemBinding) : RecyclerView.ViewHolder(
        listItemBinding.root
    ), CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            listener!!.clickListener(bindingAdapterPosition, isChecked)
        }

        init {
            itemView.box.setOnCheckedChangeListener(this)
        }
    }

    private fun setTextSallahTimes(c: Calendar): String {
        return s!!.format(c.time)
    }

    interface prayClickListener {
        fun clickListener(pos: Int, checked: Boolean)
    }

    companion object {
        private var listener: prayClickListener? = null
    }
}