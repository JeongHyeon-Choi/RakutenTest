package com.jhyun.rakuten.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.jhyun.rakuten.data.local.NoteDB
import com.jhyun.rakuten.data.local.NoteDao
import com.jhyun.rakuten.data.local.model.toModel
import com.jhyun.rakuten.domain.util.TestUtils
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import kotlin.random.Random

@RunWith(AndroidJUnit4::class)
class NoteDBTest {
    private lateinit var noteDB: NoteDB
    private lateinit var noteDao: NoteDao

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        noteDB = Room.inMemoryDatabaseBuilder(context, NoteDB::class.java).build()
        noteDao = noteDB.noteDao()
    }

    @After
    fun closeDb() {
        noteDB.close()
    }

    @Test
    fun insert_note_data() = runBlocking {
        val model = TestUtils.generateNoteEntity(0).toModel()

        noteDao.createNote(model)

        val result = noteDao.selectNoteList()

        assertEquals(1, result.size)
        assertEquals(model.title, result[0].title)
    }

    @Test
    fun insert_note_data_id_auto_generate() = runBlocking {

        noteDao.createNote(TestUtils.generateNoteEntity(0).toModel())
        noteDao.createNote(TestUtils.generateNoteEntity(0).toModel())
        noteDao.createNote(TestUtils.generateNoteEntity(0).toModel())

        val result = noteDao.selectNoteList()
        val index = Random.nextInt(result.size)

        assertEquals(3, result.size)
        assertNotEquals(0, result[index].id)
    }

    @Test
    fun update_note_data() = runBlocking {
        val listSize = 10
        val entityList = TestUtils.generateNoteEntityList(listSize)

        for (i in 0 until listSize) {
            noteDao.createNote(entityList[i].toModel())
        }

        val id = Random.nextInt(listSize) + 1L

        val model = noteDao.selectNote(id)
        val newModel = model.copy(title = "it is test title", updatedAt = Date(System.currentTimeMillis() + 10_000))

        noteDao.updateNote(newModel)

        val result = noteDao.selectNote(id)

        assertEquals(newModel.title , result.title)
        assertEquals(newModel.updatedAt.time , result.updatedAt.time)
    }

    @Test
    fun remove_note_data() = runBlocking {
        val listSize = 10
        val entityList = TestUtils.generateNoteEntityList(listSize)

        for (i in 0 until listSize) {
            noteDao.createNote(entityList[i].toModel())
        }

        val id = Random.nextInt(listSize) + 1L

        val deleteModel = noteDao.selectNote(id)


        noteDao.deleteNote(deleteModel)

        val result = noteDao.selectNoteList()

        assertEquals(9 , result.size)
        result.forEach {
            assertNotEquals(id , it.id)
        }
    }
}