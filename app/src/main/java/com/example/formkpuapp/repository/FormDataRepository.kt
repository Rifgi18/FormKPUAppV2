package com.example.formkpuapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.formkpuapp.database.FormData
import com.example.formkpuapp.database.FormDataDao
import com.example.formkpuapp.database.FormDataRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FormDataRepository(application: Application) {

    private val mFormDataDao: FormDataDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FormDataRoomDatabase.getDatabase(application)
        mFormDataDao = db.formDataDao()
    }

    fun getAllDatas(): LiveData<List<FormData>> = mFormDataDao.getAllDatas()

    fun insert(formData: FormData) {
        executorService.execute { mFormDataDao.insert(formData) }
    }

    fun delete(formData: FormData) {
        executorService.execute { mFormDataDao.delete(formData) }
    }

    fun update(formData: FormData) {
        executorService.execute { mFormDataDao.update(formData) }
    }

}