package com.example.formkpuapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.formkpuapp.R
import com.example.formkpuapp.databinding.ActivityMainBinding
import com.example.formkpuapp.ui.add.AddDataActivity
import com.example.formkpuapp.ui.formdatalist.DataListActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnInfo.setOnClickListener {
            val intent = Intent(this@MainActivity, WebViewActivity::class.java)
            startActivity(intent)
        }
        binding.btnForm.setOnClickListener {
            val intent = Intent(this, AddDataActivity::class.java)
            startActivity(intent)
        }
        binding.btnData.setOnClickListener {
            val intent = Intent(this@MainActivity, DataListActivity::class.java)
            startActivity(intent)
        }
        binding.btnExit.setOnClickListener {
            finish()
        }
    }
}