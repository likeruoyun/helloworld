package com.example.networklist

import kotlinx.coroutines.delay
import kotlin.random.Random

class FakeService {
	private val random = Random(System.currentTimeMillis())

	suspend fun fetchRepos(page: Int, pageSize: Int): List<Repo> {
		// Simulate network latency
		delay(600)
		return List(pageSize) { index ->
			val base = (page - 1) * pageSize + index + 1
			val owner = randomWord().lowercase()
			val name = randomWord() + "-" + randomWord()
			Repo(
				fullName = "$owner/$name",
				description = randomSentence(8, 16),
				stars = random.nextInt(10_000, 80_000),
				forks = random.nextInt(5_000, 40_000)
			)
		}
	}

	private fun randomWord(): String {
		val letters = ('a'..'z').toList()
		val len = random.nextInt(4, 10)
		return buildString(len) {
			repeat(len) { append(letters[random.nextInt(letters.size)]) }
		}
	}

	private fun randomSentence(min: Int, max: Int): String {
		val count = random.nextInt(min, max)
		return (0 until count).joinToString(" ") { randomWord() }.replaceFirstChar { it.uppercase() } + "."
	}
}