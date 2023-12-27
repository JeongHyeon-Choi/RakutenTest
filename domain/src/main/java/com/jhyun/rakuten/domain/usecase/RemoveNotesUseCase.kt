package com.jhyun.rakuten.domain.usecase


import com.jhyun.rakuten.domain.LocalRepository
import com.jhyun.rakuten.domain.entity.NoteEntity
import javax.inject.Inject

class RemoveNotesUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    operator fun invoke(list: List<NoteEntity>)  {
        localRepository.deleteNotes(list)
    }
}