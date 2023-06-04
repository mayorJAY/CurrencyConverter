package com.josycom.mayorjay.currencyconverter.currencyconversion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.josycom.mayorjay.currencyconverter.common.domain.model.Rate
import com.josycom.mayorjay.currencyconverter.common.util.setTextContent
import com.josycom.mayorjay.currencyconverter.databinding.RateItemViewBinding
import javax.inject.Inject

class RateAdapter @Inject constructor() : ListAdapter<Rate, RateAdapter.RateViewHolder>(DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        val binding = RateItemViewBinding.inflate(LayoutInflater.from(parent.context))
        return RateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Rate>() {
        override fun areItemsTheSame(oldItem: Rate, newItem: Rate) = oldItem.code == newItem.code

        override fun areContentsTheSame(oldItem: Rate, newItem: Rate) = oldItem == newItem
    }

    class RateViewHolder(private val binding: RateItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(rate: Rate) {
            binding.apply {
                tvCurrency.text = rate.code
                tvValue.setTextContent(rate.value, rate.amount)
            }
        }
    }
}