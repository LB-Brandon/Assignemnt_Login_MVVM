package com.brandon.assignemnt_login_mvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.brandon.assignemnt_login_mvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            signup.setOnClickListener {
                UserInfoActivity.newIntent(this@MainActivity, UserInfoEntryType.CREATE).also {
                    startActivity(it)
                }
            }
            editInfo.setOnClickListener {
                UserInfoActivity.newIntent(this@MainActivity, UserInfoEntryType.UPDATE).also {
                    startActivity(it)
                }
            }
        }


    }

}