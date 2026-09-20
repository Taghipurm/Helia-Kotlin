package com.example.helia.activity

import android.os.Bundle
import com.example.helia.BuildConfig
import com.example.helia.databinding.ActivityAboutBinding

class AboutActivity : BaseActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawerMenu(binding.drawerLayout)

        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.txtVersion.text = "نسخه: ${BuildConfig.VERSION_NAME}"
    }
}