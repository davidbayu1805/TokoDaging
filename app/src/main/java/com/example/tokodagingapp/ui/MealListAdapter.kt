package com.example.tokodagingapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tokodagingapp.R
import com.example.tokodagingapp.model.Meal


class MealListAdapter(
    private val onItemClickListener: (Meal) -> Unit
): ListAdapter<Meal, MealListAdapter.MealViewHolder> (WORDS_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
    return MealViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = getItem(position)
        holder.bind(meal)
        holder.itemView.setOnClickListener {
            onItemClickListener(meal)
        }
    }

    class MealViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val namaTextView: TextView = itemView.findViewById(R.id.namaTextView)
        private val countTextView: TextView = itemView.findViewById(R.id.addressTextView)
        private val alamatTextView: TextView = itemView.findViewById(R.id.countTextView)

        fun bind(meal: Meal?) {
            namaTextView.text = meal?.name
            countTextView.text = meal?.count
            alamatTextView.text = meal?.address

        }

        companion object {
            fun create(parent: ViewGroup): MealListAdapter.MealViewHolder {
            val view: View = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_meal, parent, false)
                return MealViewHolder(view)
            }
        }
    }
    companion object {
        private val WORDS_COMPARATOR = object : DiffUtil.ItemCallback<Meal>(){
            override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}