package com.example.helia.activity

import android.os.Bundle
import com.example.helia.databinding.ActivityCustomerBinding
import com.example.helia.databinding.ActivitySettingsBinding

class SettingsActivity : BaseActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDrawerMenu(binding.drawerLayout)
        binding.btnBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        loadSettings()
        setupListeners()
    }

    // -------------------------
    // بارگذاری تنظیمات
    // -------------------------

    private fun loadSettings() {

        // تم
        when (AppSettings.getTheme(this)) {

            AppSettings.THEME_LIGHT -> {
                binding.rbLight.isChecked = true
            }

            AppSettings.THEME_DARK -> {
                binding.rbDark.isChecked = true
            }

            AppSettings.THEME_SYSTEM -> {
                binding.rbSystem.isChecked = true
            }
        }

        // نام کاربر
        binding.switchSaveUsername.isChecked =
            AppSettings.isSaveUsername(this)

        // رمز کاربر
        binding.switchSavePassword.isChecked =
            AppSettings.isSavePassword(this)

    }

    // -------------------------
    // تغییر تنظیمات
    // -------------------------

    private fun setupListeners() {

        // تغییر تم
        binding.rgTheme.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {

                binding.rbLight.id -> {
                    AppSettings.setTheme(
                        this,
                        AppSettings.THEME_LIGHT
                    )
                }

                binding.rbDark.id -> {
                    AppSettings.setTheme(
                        this,
                        AppSettings.THEME_DARK
                    )
                }

                binding.rbSystem.id -> {
                    AppSettings.setTheme(
                        this,
                        AppSettings.THEME_SYSTEM
                    )
                }
            }
        }

        // ذخیره نام کاربر
        binding.switchSaveUsername.setOnCheckedChangeListener { _, isChecked ->

            AppSettings.setSaveUsername(
                this,
                isChecked
            )
        }

        // ذخیره رمز کاربر
        binding.switchSavePassword.setOnCheckedChangeListener { _, isChecked ->

            AppSettings.setSavePassword(
                this,
                isChecked
            )
        }
    }
}