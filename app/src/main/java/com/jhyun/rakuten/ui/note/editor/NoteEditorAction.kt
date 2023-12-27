package com.jhyun.rakuten.ui.note.editor

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
sealed class NoteEditorAction : Parcelable {
    data class New(
        val createdAt: Date
    ) : NoteEditorAction()

    data class Edit(
        val id: Long
    ) : NoteEditorAction()
}