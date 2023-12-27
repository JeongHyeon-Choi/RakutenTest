package com.jhyun.rakuten.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.jhyun.rakuten.data.local.model.NoteModel
import java.util.Date

@Dao
interface NoteDao {
    @Insert
    fun createNote(note: NoteModel): Long

    @Transaction
    fun updateNote(note: NoteModel) {
        updateNote(
            id = note.id,
            title = note.title,
            body = note.body ?: "",
            updatedAt = note.updatedAt,
        )
    }

    @Query("UPDATE notes SET title = :title, body = :body, updatedAt = :updatedAt  WHERE id = :id")
    fun updateNote(id: Long, title: String, body: String, updatedAt: Date)

    @Delete
    fun deleteNote(note: NoteModel)

    @Delete
    fun deleteNotes(vararg notes: NoteModel)

    @Query("SELECT * FROM notes WHERE id == :id")
    fun selectNote(id: Long): NoteModel

    @Query("SELECT id, title, updatedAt, createdAt FROM notes")
    fun selectNoteList(): List<NoteModel>

    @Query(
        "SELECT id, title, updatedAt, createdAt FROM notes ORDER BY " +
                "CASE WHEN :isDesc = 0 THEN createdAt END ASC ," +
                "CASE WHEN :isDesc = 1 THEN createdAt END DESC"
    )
    fun selectNoteListOrderByCreatedAt(isDesc: Boolean = false): List<NoteModel>

    @Query(
        "SELECT id, title, updatedAt, createdAt FROM notes ORDER BY " +
                "CASE WHEN :isDesc = 0 THEN updatedAt END ASC ," +
                "CASE WHEN :isDesc = 1 THEN updatedAt END DESC"
    )
    fun selectNoteListOrderByUpdatedAt(isDesc: Boolean = false): List<NoteModel>

    @Query(
        "SELECT id, title, updatedAt, createdAt FROM notes ORDER BY " +
                "CASE WHEN :isDesc = 0 THEN title END ASC ," +
                "CASE WHEN :isDesc = 1 THEN title END DESC"
    )
    fun selectNoteListOrderByTitle(isDesc: Boolean = false): List<NoteModel>
}