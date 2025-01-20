package com.application.randomstringgenerator.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.application.randomstringgenerator.data.model.RandomString
import com.application.randomstringgenerator.databinding.ItemRandomStringBinding

class RandomStringAdapter (private val onDeleteClick: (RandomString) -> Unit): RecyclerView.Adapter<RandomStringAdapter.RandomStringViewHolder>() {

    private val items = mutableListOf<RandomString>()

    fun submitList(newItems: List<RandomString>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomStringViewHolder {
        val binding = ItemRandomStringBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RandomStringViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RandomStringViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class RandomStringViewHolder(
        private val binding: ItemRandomStringBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RandomString) {
            binding.textViewValue.text = item.value
            binding.textViewLength.text = "Length: ${item.length}"
            binding.textViewCreated.text = "Created: ${item.created}"

            binding.delete.setOnClickListener {
                onDeleteClick(item)
            }
        }
    }
}
