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
import android.view.View
import com.el3asas.eduapp.ui.adapters.PraiesAdapter.prayClickListener
import com.el3asas.eduapp.ui.adapters.PraiesAdapter
import androidx.fragment.app.FragmentPagerAdapter
import com.el3asas.eduapp.databinding.MainPostItemBinding
import com.el3asas.eduapp.helpers.Image
import com.el3asas.eduapp.models.Entity
import com.el3asas.eduapp.ui.adapters.SectionsPagerAdapter

class PostsAdapter(private val context: Context, private val postsViewModel: PostsViewModel) :
    RecyclerView.Adapter<luklyViewHolder>() {
    private var list: Array<Entity>?
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): luklyViewHolder {
        val binding: MainPostItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.main_post_item,
            parent,
            false
        )
        return luklyViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: luklyViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.luklyItemBinding.viewModel = list!![position]
        when (position) {
            0 -> holder.luklyItemBinding.imageViewQ.setImageBitmap(
                Image.decodeSampledBitmapFromResource(
                    context.resources, R.drawable.quraan, 100, 100
                )
            )
            1 -> holder.luklyItemBinding.imageViewQ.setImageBitmap(
                Image.decodeSampledBitmapFromResource(
                    context.resources, R.drawable.hades, 100, 100
                )
            )
            2 -> holder.luklyItemBinding.imageViewQ.setImageBitmap(
                Image.decodeSampledBitmapFromResource(
                    context.resources, R.drawable.books, 100, 100
                )
            )
        }
        holder.luklyItemBinding.likeQ.setOnCheckedChangeListener { compoundButton: CompoundButton, b: Boolean ->
            loveBtn(
                compoundButton,
                position,
                b
            )
        }
    }

    override fun getItemCount(): Int {
        return if (list != null) list!!.size else 0
    }

    class luklyViewHolder(var luklyItemBinding: MainPostItemBinding) : RecyclerView.ViewHolder(
        luklyItemBinding.root
    )

    @SuppressLint("NotifyDataSetChanged")
    fun setList(l: Array<Entity>?) {
        list = l
        notifyDataSetChanged()
    }

    fun loveBtn(btn: View, pos: Int, s: Boolean) {
        postsViewModel.update(pos, s)
        setAnimation(btn)
    }

    companion object {
        var pos = 0
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