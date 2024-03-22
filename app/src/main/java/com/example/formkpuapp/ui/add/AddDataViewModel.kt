package com.example.formkpuapp.ui.add

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.formkpuapp.database.FormData
import com.example.formkpuapp.repository.FormDataRepository

class AddDataViewModel(application: Application) : ViewModel() {

    private val mFormDataRepository: FormDataRepository = FormDataRepository(application)

    fun insert(formData: FormData) {
        mFormDataRepository.insert(formData)
    }

    fun update(formData: FormData) {
        mFormDataRepository.update(formData)
    }

    fun delete(formData: FormData) {
        mFormDataRepository.delete(formData)
    }

}