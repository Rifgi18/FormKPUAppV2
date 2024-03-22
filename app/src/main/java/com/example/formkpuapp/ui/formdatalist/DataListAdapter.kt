package com.example.formkpuapp.ui.formdatalist

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.formkpuapp.database.FormData
import com.example.formkpuapp.databinding.ItemDataBinding
import com.example.formkpuapp.helper.NoteDiffCallback
import com.example.formkpuapp.ui.add.AddDataActivity

class DataListAdapter : RecyclerView.Adapter<DataListAdapter.FormDataViewHolder>() {

    private val formDataList = ArrayList<FormData>()
    fun setListFormData(listFormData: List<FormData>) {
        val diffCallback = NoteDiffCallback(this.formDataList, listFormData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.formDataList.clear()
        this.formDataList.addAll(listFormData)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormDataViewHolder {
        val binding = ItemDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FormDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FormDataViewHolder, position: Int) {
        holder.bind(formDataList[position])
    }

    override fun getItemCount(): Int = formDataList.size

    inner class FormDataViewHolder(private val binding: ItemDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(formData: FormData){
            with(binding) {
                tvItemDate.text = formData.date
                tvItemNama.text = formData.nama
                tvItemGender.text = formData.gender
                cvItemData.setOnClickListener {
                    val intent = Intent(it.context, AddDataActivity::class.java)
                    intent.putExtra(AddDataActivity.EXTRA_FORM, formData)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}