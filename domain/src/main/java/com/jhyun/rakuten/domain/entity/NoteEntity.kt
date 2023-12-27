package com.jhyun.rakuten.domain.entity

import java.util.Date


data class NoteEntity(
    val id: Long,
    val title: String,
    val body: String,
    val updatedAt: Date,
    val createdAt: Date,
)