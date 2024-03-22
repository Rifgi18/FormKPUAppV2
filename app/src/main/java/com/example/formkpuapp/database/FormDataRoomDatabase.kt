package com.example.formkpuapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FormData::class], version = 1)
abstract class FormDataRoomDatabase : RoomDatabase() {
    abstract fun formDataDao(): FormDataDao


    companion object {
        @Volatile
        private var INSTANCE: FormDataRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FormDataRoomDatabase {
            if (INSTANCE == null) {
                synchronized(FormDataRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FormDataRoomDatabase::class.java, "formdata_database")
                        .build()
                }
            }
            return INSTANCE as FormDataRoomDatabase
        }
    }
}