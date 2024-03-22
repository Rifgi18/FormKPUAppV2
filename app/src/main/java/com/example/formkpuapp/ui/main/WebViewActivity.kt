package com.example.formkpuapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.example.formkpuapp.R
import com.example.formkpuapp.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.wbKpu.loadUrl("https://www.kpu.go.id/")
        binding.wbKpu.settings.javaScriptEnabled = true
        binding.wbKpu.webViewClient = WebViewClient()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(binding.wbKpu.canGoBack()){
            binding.wbKpu.goBack()
        } else {
            finish()
        }
    }
}