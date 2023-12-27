package com.jhyun.rakuten.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jhyun.rakuten.data.local.converter.DateConverters
import com.jhyun.rakuten.data.local.model.NoteModel

@Database(entities = [NoteModel::class], version = 1)
@TypeConverters(DateConverters::class)
abstract class NoteDB  : RoomDatabase(){
    abstract fun noteDao(): NoteDao
}