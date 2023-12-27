package com.jhyun.rakuten

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jhyun.rakuten.domain.usecase.CreateNoteUseCase
import com.jhyun.rakuten.domain.usecase.GetNoteUseCase
import com.jhyun.rakuten.domain.usecase.UpdateNoteUseCase
import com.jhyun.rakuten.domain.util.TestUtils
import com.jhyun.rakuten.ui.note.editor.NoteEditorAction
import com.jhyun.rakuten.ui.note.editor.NoteEditorViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import java.util.Date
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class NoteEditorViewModelTest {


    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var getNoteUseCase: GetNoteUseCase

    @Mock
    lateinit var createNoteUseCase: CreateNoteUseCase

    @Mock
    lateinit var updateNoteUseCase: UpdateNoteUseCase

    private lateinit var viewModel: NoteEditorViewModel

    @Before
    fun before() {
        MockitoAnnotations.openMocks(this)

        Dispatchers.setMain(StandardTestDispatcher())
        viewModel = NoteEditorViewModel(getNoteUseCase, createNoteUseCase, updateNoteUseCase)

        doReturn(TestUtils.generateNoteEntity(id = 1)).whenever(getNoteUseCase).invoke(any())

        doReturn(1L).whenever(createNoteUseCase).invoke(any(), any(), any())

        doNothing().whenever(updateNoteUseCase).invoke(any(), any(), any())
    }

    @After
    fun after() {
        Dispatchers.resetMain()
    }

    @Test
    fun `set origin note data`() = runBlocking {
        val latch = CountDownLatch(2)

        viewModel.title.observeForever {
            if (it.isNotEmpty()) latch.countDown()
        }
        viewModel.body.observeForever {
            if (it.isNotEmpty()) latch.countDown()
        }

        viewModel.setNoteEditorAction(NoteEditorAction.Edit(1))

        latch.await(2, TimeUnit.SECONDS)

        assertFalse(viewModel.title.value.isNullOrBlank())
        assertFalse(viewModel.body.value.isNullOrBlank())
    }

    @Test
    fun `title,body edit and edited check true`() = runTest {
        val latch = CountDownLatch(1)
        var result = false

        viewModel.isEdited.observeForever {
            result = it
            latch.countDown()
        }

        viewModel.setNoteEditorAction(NoteEditorAction.New(Date()))

        viewModel.title.value = "test title"
        viewModel.body.value = "test body"

        latch.await(2, TimeUnit.SECONDS)

        assertTrue(result)
    }

}