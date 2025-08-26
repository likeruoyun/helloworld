package com.example.networklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.networklist.databinding.ItemRepoBinding

class RepoAdapter : RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {

	private val items: MutableList<Repo> = mutableListOf()

	fun submit(newItems: List<Repo>, append: Boolean) {
		if (!append) items.clear()
		items.addAll(newItems)
		notifyDataSetChanged()
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
		val binding = ItemRepoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return RepoViewHolder(binding)
	}

	override fun getItemCount(): Int = items.size

	override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
		holder.bind(items[position])
	}

	class RepoViewHolder(private val binding: ItemRepoBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(repo: Repo) {
			binding.textHeader.text = repo.fullName
			binding.textDescription.text = repo.description
			binding.textStars.text = repo.stars.toString()
			binding.textForks.text = repo.forks.toString()
		}
	}
}