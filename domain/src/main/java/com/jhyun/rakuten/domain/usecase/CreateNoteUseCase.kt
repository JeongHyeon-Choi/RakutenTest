package com.jhyun.rakuten.domain.usecase


import com.jhyun.rakuten.domain.LocalRepository
import com.jhyun.rakuten.domain.entity.NoteEntity
import java.util.Date
import javax.inject.Inject

class CreateNoteUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    operator fun invoke(title: String, body: String, createdAt: Date): Long {
        return localRepository.insertNote(
            NoteEntity(
                id = 0,
                title = title,
                body = body,
                updatedAt = Date(),
                createdAt = createdAt,
            )
        )
    }
}