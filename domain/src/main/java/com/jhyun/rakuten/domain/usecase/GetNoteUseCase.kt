package com.jhyun.rakuten.domain.usecase


import com.jhyun.rakuten.domain.LocalRepository
import com.jhyun.rakuten.domain.entity.NoteEntity
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    operator fun invoke(id: Long): NoteEntity {
        return localRepository.selectNote(id)
    }
}