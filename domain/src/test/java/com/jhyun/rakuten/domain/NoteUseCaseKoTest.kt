package com.jhyun.rakuten.domain

import com.jhyun.rakuten.domain.usecase.CreateNoteUseCase
import com.jhyun.rakuten.domain.usecase.GetNoteUseCase
import com.jhyun.rakuten.domain.usecase.GetNotesUseCase
import com.jhyun.rakuten.domain.usecase.RemoveNotesUseCase
import com.jhyun.rakuten.domain.usecase.UpdateNoteUseCase
import com.jhyun.rakuten.domain.util.TestUtils
import io.kotest.core.annotation.Tags
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import java.util.Date

@Tags("UseCase")
class NoteUseCaseKoTest : StringSpec({

    val mockRepository = mock<LocalRepository> { }

    val createNoteUseCase = CreateNoteUseCase(mockRepository)
    val getNoteUseCase = GetNoteUseCase(mockRepository)
    val getNotesUseCase = GetNotesUseCase(mockRepository)
    val removeNotesUseCase = RemoveNotesUseCase(mockRepository)
    val updateNoteUseCase = UpdateNoteUseCase(mockRepository)

    `when`(mockRepository.insertNote(any())).thenReturn(1)
    `when`(mockRepository.selectNote(any())).thenReturn(TestUtils.generateNoteEntity(1))
    `when`(mockRepository.selectNotes(any())).thenReturn(TestUtils.generateNoteEntityList(10))

    "create note usecase return id 1?" {
        createNoteUseCase("test title", "test body", Date()) shouldBe 1
    }

    "get note usecase return note entity" {
        val result = getNoteUseCase(1)

        result.id shouldBe 1
    }

    "get notes usecase return note entity list" {
        val result = getNotesUseCase(sortType = NoteSortType.TITLE_DESC)

        result.size shouldBe 10
        result.last().id shouldBe 10
    }


})
