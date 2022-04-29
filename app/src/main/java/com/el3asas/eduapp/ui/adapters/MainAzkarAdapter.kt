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
import android.widget.ImageView
import com.el3asas.eduapp.ui.adapters.PraiesAdapter.prayClickListener
import com.el3asas.eduapp.ui.adapters.PraiesAdapter
import androidx.fragment.app.FragmentPagerAdapter
import com.el3asas.eduapp.ui.adapters.SectionsPagerAdapter

/*** Created by el3sas on 11/24/2021 ,
 */
class MainAzkarAdapter(cardListener: CardListener?, private val items: List<MainAzkarItem>?) :
    RecyclerView.Adapter<MainAzkarAdapterViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MainAzkarAdapterViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.main_azkar_item, parent, false)
        return MainAzkarAdapterViewHolder(v)
    }

    override fun onBindViewHolder(holder: MainAzkarAdapterViewHolder, position: Int) {
        val item = items!![position]
        holder.set(item)
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    class MainAzkarAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var textView: TextView
        var img: ImageView
        fun set(item: MainAzkarItem) {
            //UI setting code
            textView.text = item.azkarName
            img.setImageResource(item.imgRes)
        }

        override fun onClick(view: View) {
            cardListener!!.CardClickListener(bindingAdapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
            textView = itemView.findViewById(R.id.title)
            img = itemView.findViewById(R.id.img)
        }
    }

    interface CardListener {
        fun CardClickListener(position: Int)
    }

    companion object {
        private var cardListener: CardListener? = null
    }

    init {
        Companion.cardListener = cardListener
    }
}