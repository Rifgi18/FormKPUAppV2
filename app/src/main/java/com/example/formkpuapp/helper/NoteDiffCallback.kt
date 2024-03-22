package com.example.formkpuapp.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.formkpuapp.database.FormData

class NoteDiffCallback(private val mOldFormDataList: List<FormData>, private val mNewFormDataList: List<FormData>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return mOldFormDataList.size
    }

    override fun getNewListSize(): Int {
        return mNewFormDataList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldFormDataList[oldItemPosition].id == mNewFormDataList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldEmployee = mOldFormDataList[oldItemPosition]
        val newEmployee = mNewFormDataList[newItemPosition]
        return oldEmployee.nik == newEmployee.nik && oldEmployee.nama == newEmployee.nama && oldEmployee.noHand == newEmployee.noHand && oldEmployee.gender == newEmployee.gender && oldEmployee.tanggal == newEmployee.tanggal && oldEmployee.lokasi == newEmployee.lokasi && oldEmployee.latitude == newEmployee.latitude && oldEmployee.longitude == newEmployee.longitude && oldEmployee.gambar.contentEquals(
            newEmployee.gambar
        ) && oldEmployee.date == newEmployee.date
    }
}