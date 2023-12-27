package com.jhyun.rakuten

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jhyun.rakuten.domain.NoteSortType
import com.jhyun.rakuten.domain.entity.NoteEntity
import com.jhyun.rakuten.domain.usecase.GetNotesUseCase
import com.jhyun.rakuten.domain.usecase.RemoveNotesUseCase
import com.jhyun.rakuten.domain.util.TestUtils
import com.jhyun.rakuten.ui.note.NoteListData
import com.jhyun.rakuten.ui.note.NoteListViewModel
import com.jhyun.rakuten.ui.note.editor.NoteEditorAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@ExperimentalCoroutinesApi
class NoteListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var getNotesUseCase: GetNotesUseCase

    @Mock
    lateinit var removeNotesUseCase: RemoveNotesUseCase

    private lateinit var viewModel: NoteListViewModel


    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)

        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = NoteListViewModel(getNotesUseCase, removeNotesUseCase)

        doReturn(TestUtils.generateNoteEntityList(20)).whenever(getNotesUseCase).invoke(any())

        doNothing().whenever(removeNotesUseCase).invoke(any())
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `click delete button and show alert`() = runTest {
        val latch = CountDownLatch(1)
        var result : List<NoteEntity> = listOf()

        viewModel.noteList.observeForever {
            it[0].setSelected(true)
            it[1].setSelected(true)
            viewModel.clickDeleteNote()
        }
        viewModel.deleteAlertEvent.observeForever {
            result = it
            latch.countDown()
        }

        viewModel.setNoteSortType(NoteSortType.CREATED_AT_ASC)
        viewModel.getNotes()

        latch.await(2, TimeUnit.SECONDS)

        assertEquals(2, result.size)
    }

    @Test
    fun `click delete button and send empty delete item toast`() = runTest {
        val latch = CountDownLatch(1)
        var result = ""

        viewModel.noteList.observeForever {
            viewModel.clickDeleteNote()
        }
        viewModel.toastEvent.observeForever {
            result = it
            latch.countDown()
        }

        viewModel.setNoteSortType(NoteSortType.CREATED_AT_ASC)
        viewModel.getNotes()

        latch.await(2, TimeUnit.SECONDS)

        assertTrue(result == "Select Delete Note Item")
    }

    @Test
    fun `delete notes`() = runTest {
        val deleteList = listOf<NoteEntity>()
        viewModel.deleteNote(deleteList)

        viewModel.clickDeleteNote()

        verify(removeNotesUseCase, times(1)).invoke(deleteList)
    }

    @Test
    fun `click add button and move new event`() = runTest {
        val latch = CountDownLatch(1)
        var result : NoteEditorAction? = null

        viewModel.moveEditorEvent.observeForever {
            result = it
            latch.countDown()
        }
        viewModel.clickAddNote()

        latch.await(2, TimeUnit.SECONDS)

        assertTrue(result is NoteEditorAction.New)
    }

    @Test
    fun `click note item and move edit event`() = runTest {
        val latch = CountDownLatch(1)
        var result : NoteEditorAction? = null

        viewModel.noteList.observeForever {
//            viewModel.clickLongNoteItem(it.first())
            viewModel.clickNoteItem(it.first())
        }
        viewModel.moveEditorEvent.observeForever {
            result = it
            latch.countDown()
        }

        viewModel.setNoteSortType(NoteSortType.CREATED_AT_ASC)
        viewModel.getNotes()

        latch.await(2, TimeUnit.SECONDS)

        assertTrue(result is NoteEditorAction.Edit)
    }

    @Test
    fun `in selection mode, click and select the first item`() = runTest {
        val latch = CountDownLatch(1)
        var result : NoteListData? = null

        viewModel.noteList.observeForever {
            result = it.first()
            viewModel.clickLongNoteItem(result!!)
            viewModel.clickNoteItem(result!!)
            latch.countDown()
        }

        viewModel.setNoteSortType(NoteSortType.CREATED_AT_ASC)
        viewModel.getNotes()

        latch.await(2, TimeUnit.SECONDS)

        assertTrue(result?.selected == true)
    }


}