package com.jhyun.rakuten.domain.usecase


import com.jhyun.rakuten.domain.LocalRepository
import com.jhyun.rakuten.domain.NoteSortType
import com.jhyun.rakuten.domain.entity.NoteEntity
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {

    operator fun invoke(sortType: NoteSortType): List<NoteEntity> {
        return localRepository.selectNotes(sortType)
    }
}