package com.jhyun.rakuten.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jhyun.rakuten.domain.entity.NoteEntity
import java.util.Date

@Entity(tableName = "notes")
data class NoteModel(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String = "",
    val body: String? = "",
    val updatedAt: Date,
    val createdAt: Date,
)

fun NoteModel.toEntity(): NoteEntity = NoteEntity(
    id = this.id,
    title = this.title,
    body = this.body ?: "",
    updatedAt = this.updatedAt,
    createdAt = this.createdAt,
)

fun NoteEntity.toModel(): NoteModel = NoteModel(
    id = this.id,
    title = this.title,
    body = this.body,
    updatedAt = this.updatedAt,
    createdAt = this.createdAt,
)
