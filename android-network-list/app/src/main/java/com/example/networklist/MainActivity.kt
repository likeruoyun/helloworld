package com.example.networklist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.networklist.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private val adapter = RepoAdapter()
	private val service = FakeService()
	private var page: Int = 1
	private val pageSize: Int = 20
	private var isLoading: Boolean = false

	private val uiScope = CoroutineScope(Job() + Dispatchers.Main)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		binding.toolbar.title = getString(R.string.title_paging)
		binding.recyclerView.adapter = adapter
		binding.recyclerView.layoutManager = LinearLayoutManager(this)

		binding.swipeRefresh.setOnRefreshListener {
			page = 1
			loadData(reset = true)
		}

		binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				super.onScrolled(recyclerView, dx, dy)
				if (dy <= 0) return
				val lm = recyclerView.layoutManager as LinearLayoutManager
				val lastVisible = lm.findLastVisibleItemPosition()
				val total = lm.itemCount
				if (!isLoading && lastVisible >= total - 5) {
					page += 1
					loadData(reset = false)
				}
			}
		})

		loadData(reset = true)
	}

	private fun loadData(reset: Boolean) {
		isLoading = true
		if (reset) binding.swipeRefresh.isRefreshing = true
		uiScope.launch {
			val data = service.fetchRepos(page = page, pageSize = pageSize)
			adapter.submit(data, append = !reset)
			if (reset) binding.swipeRefresh.isRefreshing = false
			isLoading = false
		}
	}
}