package com.example.formkpuapp.ui.formdatalist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.formkpuapp.database.FormData
import com.example.formkpuapp.repository.FormDataRepository

class DataListViewModel (application: Application) : ViewModel() {

    private val mFormDataRepository: FormDataRepository = FormDataRepository(application)

    fun getAllDatas(): LiveData<List<FormData>> = mFormDataRepository.getAllDatas()

}