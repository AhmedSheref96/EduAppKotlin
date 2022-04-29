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
import android.widget.Filter
import android.widget.Filterable
import com.el3asas.eduapp.ui.adapters.PraiesAdapter.prayClickListener
import com.el3asas.eduapp.ui.adapters.PraiesAdapter
import androidx.fragment.app.FragmentPagerAdapter
import com.el3asas.eduapp.databinding.ListItemBinding
import com.el3asas.eduapp.models.Entity
import com.el3asas.eduapp.ui.adapters.SectionsPagerAdapter
import java.util.ArrayList
import java.util.regex.Pattern

class MyAdapterQ : RecyclerView.Adapter<MyAdapterQ.MyViewHolder>, Filterable {
    private var Entity: MutableList<Entity>? = null
    private var EntityFull: List<Entity>? = null

    constructor(list: MutableList<Entity>?) {
        Entity = list
        EntityFull = ArrayList(list)
    }

    constructor(l: clickListener?) {
        listener = l
    }

    fun setList(list: MutableList<Entity>?) {
        Entity = list
        EntityFull = ArrayList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.listItemBinding.model = Entity!![position]
    }

    override fun getItemCount(): Int {
        return if (Entity != null) Entity!!.size else 0
    }

    class MyViewHolder(var listItemBinding: ListItemBinding) : RecyclerView.ViewHolder(
        listItemBinding.root
    ), View.OnClickListener {
        override fun onClick(v: View) {
            listener!!.onClickItemListerer(bindingAdapterPosition)
        }

        init {
            itemView.getRoot().setOnClickListener(this)
        }
    }

    override fun getFilter(): Filter {
        return instance
    }

    private val instance: Filter
        private get() = object : Filter() {
            public override fun performFiltering(charSequence: CharSequence): FilterResults {
                val arrayList = ArrayList<Entity>()
                if (charSequence == null || charSequence.length == 0) {
                    arrayList.addAll(EntityFull!!)
                } else {
                    val trim = charSequence.toString().toLowerCase().trim { it <= ' ' }
                    for (entity in EntityFull!!) {
                        if (Pattern.compile("[\\p{P}\\p{Mn}]").matcher(entity.quot.toLowerCase())
                                .reset().replaceAll("").replace("أ", "ا").replace("إ", "ا")
                                .replace("آ", "ا").contains(trim)
                        ) {
                            arrayList.add(entity)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = arrayList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            public override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                Entity!!.clear()
                if (filterResults.count != 0) Entity!!.addAll(filterResults.values as List<*>)
                notifyDataSetChanged()
            }
        }

    interface clickListener {
        fun onClickItemListerer(position: Int)
    }

    companion object {
        private var listener: clickListener? = null
    }
}