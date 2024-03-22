package com.example.formkpuapp.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.formkpuapp.ui.add.AddDataViewModel
import com.example.formkpuapp.ui.formdatalist.DataListViewModel

class ViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddDataViewModel::class.java)) {
            return AddDataViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(DataListViewModel::class.java)) {
            return DataListViewModel(mApplication) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

}