package com.example.networklist

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

	private lateinit var recyclerView: RecyclerView
	private val adapter: RepoAdapter = RepoAdapter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// Root layout: ConstraintLayout with a full-screen RecyclerView
		val root = ConstraintLayout(this).apply {
			layoutParams = ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT
			)
		}

		recyclerView = RecyclerView(this).apply {
			id = View.generateViewId()
			layoutManager = LinearLayoutManager(this@MainActivity)
			adapter = this@MainActivity.adapter
		}

		val rvLp = ConstraintLayout.LayoutParams(0, 0).apply {
			topToTop = ConstraintLayout.LayoutParams.PARENT_ID
			bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
			startToStart = ConstraintLayout.LayoutParams.PARENT_ID
			endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
		}
		root.addView(recyclerView, rvLp)

		setContentView(root)

		// Generate and display random data
		adapter.submitList(generateRandomRepos(40))
	}

	// ------------------------- Data -------------------------
	private data class Repo(
		val fullName: String,
		val description: String,
		val stars: Int,
		val forks: Int
	)

	private fun generateRandomRepos(count: Int): List<Repo> {
		val random = Random(System.currentTimeMillis())
		fun word(): String {
			val len = random.nextInt(4, 9)
			val sb = StringBuilder()
			repeat(len) { sb.append(('a'..'z').random(random)) }
			return sb.toString()
		}
		fun sentence(min: Int, max: Int): String {
			val n = random.nextInt(min, max)
			return (0 until n).joinToString(" ") { word() }.replaceFirstChar { it.uppercase() } + "."
		}
		return List(count) {
			Repo(
				fullName = "${word()}/${word()}-${word()}",
				description = sentence(8, 16),
				stars = random.nextInt(10_000, 80_000),
				forks = random.nextInt(5_000, 40_000)
			)
		}
	}

	// ------------------------- Adapter -------------------------
	private inner class RepoAdapter : RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {

		private val items: MutableList<Repo> = mutableListOf()

		fun submitList(newItems: List<Repo>) {
			items.clear()
			items.addAll(newItems)
			notifyDataSetChanged()
		}

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
			return RepoViewHolder(createItemView(parent))
		}

		override fun getItemCount(): Int = items.size

		override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
			holder.bind(items[position])
		}

		inner class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			private val headerText: TextView = itemView.findViewById(HEADER_ID)
			private val descriptionText: TextView = itemView.findViewById(DESC_ID)
			private val starsText: TextView = itemView.findViewById(STARS_ID)
			private val forksText: TextView = itemView.findViewById(FORKS_ID)

			fun bind(repo: Repo) {
				headerText.text = repo.fullName
				descriptionText.text = repo.description
				starsText.text = repo.stars.toString()
				forksText.text = repo.forks.toString()
			}
		}

		private fun dp(parent: ViewGroup, value: Int): Int {
			val density = parent.context.resources.displayMetrics.density
			return (value * density).toInt()
		}

		private fun createItemView(parent: ViewGroup): View {
			val padding = dp(parent, 12)
			val small = dp(parent, 6)
			val icon = dp(parent, 18)
			val dividerH = dp(parent, 1)

			val root = ConstraintLayout(parent.context).apply {
				id = View.generateViewId()
				setPadding(padding, padding, padding, padding)
				layoutParams = RecyclerView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT
				)
			}

			val header = TextView(parent.context).apply {
				id = HEADER_ID
				// Blue-ish link color
				setTextColor(Color.parseColor("#1565C0"))
				setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
				setTypeface(typeface, android.graphics.Typeface.BOLD)
			}
			root.addView(header, ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
				topToTop = ConstraintLayout.LayoutParams.PARENT_ID
				startToStart = ConstraintLayout.LayoutParams.PARENT_ID
				endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
			})

			val desc = TextView(parent.context).apply {
				id = DESC_ID
				setTextColor(Color.parseColor("#333333"))
			}
			root.addView(desc, ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
				topToBottom = HEADER_ID
				startToStart = ConstraintLayout.LayoutParams.PARENT_ID
				endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
				setMargins(0, small, 0, 0)
			})

			val starIcon = ImageView(parent.context).apply {
				id = STAR_ICON_ID
				setImageResource(android.R.drawable.btn_star_big_on)
			}
			root.addView(starIcon, ConstraintLayout.LayoutParams(icon, icon).apply {
				topToBottom = DESC_ID
				startToStart = ConstraintLayout.LayoutParams.PARENT_ID
				setMargins(0, dp(parent, 10), 0, 0)
			})

			val stars = TextView(parent.context).apply { id = STARS_ID }
			root.addView(stars, ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
				baselineToBaseline = STAR_ICON_ID
				startToEnd = STAR_ICON_ID
				setMargins(small, 0, 0, 0)
			})

			val forkIcon = ImageView(parent.context).apply {
				id = FORK_ICON_ID
				setImageResource(android.R.drawable.stat_sys_upload)
			}
			root.addView(forkIcon, ConstraintLayout.LayoutParams(icon, icon).apply {
				baselineToBaseline = STAR_ICON_ID
				startToEnd = STARS_ID
				setMargins(dp(parent, 18), 0, 0, 0)
			})

			val forks = TextView(parent.context).apply { id = FORKS_ID }
			root.addView(forks, ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
				baselineToBaseline = STAR_ICON_ID
				startToEnd = FORK_ICON_ID
				setMargins(small, 0, 0, 0)
			})

			val divider = View(parent.context).apply { id = DIVIDER_ID; setBackgroundColor(Color.parseColor("#DDDDDD")) }
			root.addView(divider, ConstraintLayout.LayoutParams(0, dividerH).apply {
				topToBottom = FORKS_ID
				startToStart = ConstraintLayout.LayoutParams.PARENT_ID
				endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
				setMargins(0, dp(parent, 12), 0, 0)
			})

			return root
		}

		companion object {
			private val HEADER_ID = View.generateViewId()
			private val DESC_ID = View.generateViewId()
			private val STAR_ICON_ID = View.generateViewId()
			private val STARS_ID = View.generateViewId()
			private val FORK_ICON_ID = View.generateViewId()
			private val FORKS_ID = View.generateViewId()
			private val DIVIDER_ID = View.generateViewId()
		}
	}
}