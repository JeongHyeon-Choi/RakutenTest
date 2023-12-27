package com.jhyun.rakuten.di


import android.content.Context
import androidx.room.Room
import com.jhyun.rakuten.data.local.NoteDB
import com.jhyun.rakuten.data.local.NoteDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@Module(includes = [RepositoryModule::class])
@InstallIn(SingletonComponent::class)
class AppModule {


    @Provides
    @Singleton
    fun provideNoteDB(@ApplicationContext context: Context): NoteDB {
        return Room.databaseBuilder(
            context,
            NoteDB::class.java,
            "note db"
        ).build()
    }


    @Provides
    @Singleton
    fun provideNoteDao(noteDB: NoteDB): NoteDao {
        return noteDB.noteDao()
    }




}