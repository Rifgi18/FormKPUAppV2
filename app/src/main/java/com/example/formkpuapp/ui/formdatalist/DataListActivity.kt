package com.example.formkpuapp.ui.formdatalist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.formkpuapp.R
import com.example.formkpuapp.databinding.ActivityDataListBinding
import com.example.formkpuapp.helper.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class DataListActivity : AppCompatActivity() {

    private var _activityDataListBinding: ActivityDataListBinding? = null
    private val binding get() = _activityDataListBinding
    private lateinit var adapter: DataListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityDataListBinding = ActivityDataListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        adapter = DataListAdapter()

        binding?.rvFormData?.layoutManager = LinearLayoutManager(this)
        binding?.rvFormData?.setHasFixedSize(true)
        binding?.rvFormData?.adapter = adapter


        val dataListViewModel = obtainViewModel(this@DataListActivity)
        dataListViewModel.getAllDatas().observe(this) {
            if (it != null && it.isNotEmpty()){
                adapter.setListFormData(it)
            } else {
                adapter.setListFormData(emptyList())
                showSnackbarMessage(getString(R.string.listNotice))
            }
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): DataListViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[DataListViewModel::class.java]
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityDataListBinding = null
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding?.rvFormData!!, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}