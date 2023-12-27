package com.jhyun.rakuten.domain.usecase


import com.jhyun.rakuten.domain.LocalRepository
import com.jhyun.rakuten.domain.entity.NoteEntity
import java.util.Date
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    operator fun invoke(id: Long, title: String, body: String) {
        localRepository.updateNote(
            NoteEntity(
                id = id,
                title = title,
                body = body,
                updatedAt = Date(),
                createdAt = Date()
            )
        )
    }
}