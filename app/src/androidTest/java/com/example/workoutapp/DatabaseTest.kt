package com.example.workoutapp

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.workoutapp.data.AppDatabase
import com.example.workoutapp.data.NoteDao
import com.example.workoutapp.data.NoteEntity
import com.example.workoutapp.data.SampleDataProvider
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var dao: NoteDao
    private lateinit var database: AppDatabase

    @Before
    fun createDb(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(appContext, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.noteDao()!!
    }
    @Test
    fun CreateNotes() {
        dao.insertAll(SampleDataProvider.getNotes())
        val count = dao.getCount()
        assertEquals(count, SampleDataProvider.getNotes().size)
    }
    @Test
    fun insertNote(){
        val note = NoteEntity()
        note.text = "some text"
        dao.insertNote(note)
        val savedNote = dao.getNoteById(1)
        assertEquals(savedNote?.id?:0, 1)
    }
    @After
    fun closeDb(){
        database.close()
    }
}