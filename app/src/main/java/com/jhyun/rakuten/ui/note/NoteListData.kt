package com.jhyun.rakuten.ui.note

import com.jhyun.rakuten.domain.entity.NoteEntity
import com.jhyun.rakuten.util.DateExt.toSimpleFormat
import java.util.Date


data class NoteListData(
    val id: Long = 0,
    val title: String = "",
    val updatedAt: String = "",
    val createdAt: String = "",
) {
    var selected: Boolean = false

    fun reverseSelected(): NoteListData {
        selected = !selected
        return this
    }

    fun setSelected(bool: Boolean): NoteListData {
        selected = bool
        return this
    }
}

fun NoteEntity.toData() = NoteListData(
    id = this.id,
    title = this.title,
    updatedAt = this.updatedAt.toSimpleFormat(),
    createdAt = this.createdAt.toSimpleFormat(),
)

fun NoteListData.toEntity() = NoteEntity(
    id = id,
    title = "",
    body = "",
    updatedAt = Date(),
    createdAt = Date(),
)
