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
import android.util.Log
import android.view.View
import com.el3asas.eduapp.ui.adapters.PraiesAdapter.prayClickListener
import com.el3asas.eduapp.ui.adapters.PraiesAdapter
import androidx.fragment.app.FragmentPagerAdapter
import com.el3asas.eduapp.databinding.ZekrItemBinding
import com.el3asas.eduapp.db.Repository
import com.el3asas.eduapp.ui.adapters.SectionsPagerAdapter
import io.reactivex.rxjava3.schedulers.Schedulers

class CatAzkarRVAdapter : RecyclerView.Adapter<CatAzkarRecyViewHolder>() {
    private var list: Array<AzkarEntity>?
    private var disposable: Disposable? = null
    var repository: Repository? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatAzkarRecyViewHolder {
        val binding: ZekrItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.zekr_item,
            parent,
            false
        )
        return CatAzkarRecyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CatAzkarRecyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.zerItemBinding.viewModel = list!![position]
        holder.zerItemBinding.likeZ.setOnClickListener { view: View ->
            setAnimation(view)
            disposable = repository!!.updateStatus(
                holder.zerItemBinding.likeZ.isChecked,
                list!![position].id
            )
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(
                    {
                        Log.d("", "onBindViewHolder: --$itemCount")
                        list!![position].isStatus = holder.zerItemBinding.likeZ.isChecked
                    }
                ) { throwable: Throwable? -> Log.d("", "onBindViewHolder: error----") }
        }
    }

    override fun getItemCount(): Int {
        return if (list != null) list!!.size else 0
    }

    class CatAzkarRecyViewHolder(var zerItemBinding: ZekrItemBinding) : RecyclerView.ViewHolder(
        zerItemBinding.root
    )

    @SuppressLint("NotifyDataSetChanged")
    fun setList(l: Array<AzkarEntity>?) {
        list = l
        notifyDataSetChanged()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        disposable!!.dispose()
    }

    companion object {
        private fun setAnimation(v: View) {
            val anim = ValueAnimator.ofFloat(1f, .7f)
            anim.duration = 150
            anim.addUpdateListener { animation: ValueAnimator ->
                v.scaleX = (animation.animatedValue as Float)
                v.scaleY = (animation.animatedValue as Float)
            }
            anim.repeatCount = 1
            anim.repeatMode = ValueAnimator.REVERSE
            anim.start()
        }
    }
}