package com.example.helia.activity

import android.content.Intent
import android.os.Bundle
import android.view.PixelCopy.request
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.DragAndDropPermissionsCompat.request
import androidx.lifecycle.lifecycleScope
import com.example.helia.R
import com.example.helia.data.PreferencesManager
import com.example.helia.databinding.ActivityLoginBinding
import com.example.helia.network.LoginRequest
import com.example.helia.network.RetrofitClient
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity(){
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val savedUserName =
            PreferencesManager.getUserName(this)
        val savedPassword =
            PreferencesManager.getPassword(this)

        if (savedUserName.isNotEmpty()) {

            binding.edtUserName.setText(savedUserName)

//            binding.edtPassword.requestFocus()
            binding.edtPassword.setText(savedPassword)

            binding.edtUserName.setTextColor(
                ContextCompat.getColor(this, R.color.black)
            )
            binding.edtPassword.setTextColor(
                ContextCompat.getColor(this, R.color.black)
            )

        }
        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch {
                try {

//                    Toast.makeText(applicationContext, "MY Point1", Toast.LENGTH_LONG).show()

                    val request = LoginRequest(
//                        username = binding.edtUserName.text.toString().trim(),
//                        password = binding.edtPassword.text.toString()
                        username = convertNumToEnglish(binding.edtUserName.text.toString().trim()),
                        password = convertNumToEnglish(binding.edtPassword.text.toString())
                    )
                    val result =
                        RetrofitClient.api.login(request)

                    if (result.success) {
//                    if (true) {

                        result.data?.let { user ->
                            PreferencesManager.saveUser(
                                this@LoginActivity,
                                user.userID,
                                user.userName,
                                binding.edtPassword.text.toString()
                            )

//                            Toast.makeText(
//                                this@LoginActivity,
//                                "ورود موفق بود",
//                                Toast.LENGTH_LONG
//                            ).show()

                            val intent =
                                Intent(
                                    this@LoginActivity,
                                    CustomerActivity::class.java
                                )

                            startActivity(intent)

//                            finish()

                        }

                    }
                    else {

                        Toast.makeText(
                            this@LoginActivity,
//                            result.message,
                            "نام کاربری یا رمز ورود اشتباه است",
                            Toast.LENGTH_LONG
                        ).show()

                    }

                }
                catch(e:Exception){
                    Toast.makeText(
                        this@LoginActivity,
//                        e.message,
//                        "خطای: 2",
                        "ارتباط با بانک اطلاعاتی برقرار نشد",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun convertNumToEnglish(input: String): String {
        return input.replace('۰', '0')
            .replace('۱', '1')
            .replace('۲', '2')
            .replace('۳', '3')
            .replace('۴', '4')
            .replace('۵', '5')
            .replace('۶', '6')
            .replace('۷', '7')
            .replace('۸', '8')
            .replace('۹', '9')
    }


}