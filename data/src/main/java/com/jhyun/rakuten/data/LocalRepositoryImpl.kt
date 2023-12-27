package com.jhyun.rakuten.data


import com.jhyun.rakuten.data.local.NoteDao
import com.jhyun.rakuten.data.local.model.toEntity
import com.jhyun.rakuten.data.local.model.toModel
import com.jhyun.rakuten.domain.LocalRepository
import com.jhyun.rakuten.domain.NoteSortType
import com.jhyun.rakuten.domain.entity.NoteEntity
import javax.inject.Inject


class LocalRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : LocalRepository {

    override fun insertNote(entity: NoteEntity): Long {
        return noteDao.createNote(entity.toModel())
    }

    override fun selectNote(id: Long): NoteEntity {
        return noteDao.selectNote(id).toEntity()
    }

    override fun selectNotes(type: NoteSortType): List<NoteEntity> {
        return when (type) {
            NoteSortType.CREATED_AT_ASC -> noteDao.selectNoteListOrderByCreatedAt().map { it.toEntity() }
            NoteSortType.CREATED_AT_DESC -> noteDao.selectNoteListOrderByCreatedAt(isDesc = true).map { it.toEntity() }
            NoteSortType.UPDATED_AT_ASC -> noteDao.selectNoteListOrderByUpdatedAt().map { it.toEntity() }
            NoteSortType.UPDATED_AT_DESC -> noteDao.selectNoteListOrderByUpdatedAt(isDesc = true).map { it.toEntity() }
            NoteSortType.TITLE_ASC -> noteDao.selectNoteListOrderByTitle().map { it.toEntity() }
            NoteSortType.TITLE_DESC -> noteDao.selectNoteListOrderByTitle(isDesc = true).map { it.toEntity() }
            else -> noteDao.selectNoteList().map { it.toEntity() }

        }
    }

    override fun updateNote(entity: NoteEntity) {
        noteDao.updateNote(entity.toModel())
    }

    override fun deleteNotes(list: List<NoteEntity>) {
        noteDao.deleteNotes(*list.map { it.toModel() }.toTypedArray())
    }


}