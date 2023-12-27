package com.jhyun.rakuten.domain.util

import com.jhyun.rakuten.domain.entity.NoteEntity
import org.jetbrains.annotations.TestOnly
import java.util.Date
import java.util.Random

object TestUtils {
    @TestOnly
    fun generateNoteEntityList(size: Int): List<NoteEntity> {
        return mutableListOf<NoteEntity>().apply {
            for (id in 1..size) {
                add(generateNoteEntity(id.toLong()))
            }
        }
    }

    @TestOnly
    fun generateNoteEntity(id: Long): NoteEntity = NoteEntity(
        id = id,
        title = "${generateRandomString()} $id title",
        body = generateRandomString(),
        updatedAt = Date(),
        createdAt = Date(),
    )

    private fun generateRandomString(): String {
        val random = Random()
        val length = random.nextInt(3) + 3 // 3 to 5
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

        return (1..length)
            .map { random.nextInt(charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }
}