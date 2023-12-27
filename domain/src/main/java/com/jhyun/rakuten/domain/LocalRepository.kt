package com.jhyun.rakuten.domain

import com.jhyun.rakuten.domain.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    fun insertNote(entity: NoteEntity) : Long

    fun selectNote(id: Long):NoteEntity

    fun selectNotes(type: NoteSortType): List<NoteEntity>

    fun updateNote(entity: NoteEntity)

    fun deleteNotes(list: List<NoteEntity>)
}