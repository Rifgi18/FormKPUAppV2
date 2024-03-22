package com.example.formkpuapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface FormDataDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(formData: FormData)

    @Update
    fun update(formData: FormData)

    @Delete
    fun delete(formData: FormData)

    @Query("SELECT * from formdata ORDER BY id ASC")
    fun getAllDatas(): LiveData<List<FormData>>
}