package com.brandon.assignemnt_login_mvvm

import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.MutableLiveData
import com.brandon.assignemnt_login_mvvm.LiveDataUtil.updateStringData
import com.brandon.assignemnt_login_mvvm.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    val TAG = "Main"

    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    private val viewModel: SignupViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        observing()
    }


    private fun initViews() {
        with(binding) {
            etName.addTextChangedListener {
                handleTextChanged(viewModel.username, it)
            }
            etEmail.addTextChangedListener {
                handleTextChanged(viewModel.userEmail, it)
            }
            etPassword.addTextChangedListener {
                viewModel.userPassword.updateStringData(it.toString())
            }
            etPasswordConfirm.addTextChangedListener {
                viewModel.userPasswordConfirm.updateStringData(it.toString())
            }
        }
    }

    private fun observing() {
        // 사용자 입력 텍스트 변경 감시
        with(viewModel) {
            username.observe(this@SignupActivity) {
                getMessageValidName(it)
            }
            userEmail.observe(this@SignupActivity) {
                getMessageValidEmail(it)
            }
        }

        // 사용자 입력으로 인한 Error 메시지 변경 감시
        with(viewModel){
            usernameErrorMessage.observe(this@SignupActivity){
                Log.d(TAG, "Error 메시지 입감")
                binding.tvNameError.text = it.asString(this@SignupActivity)
            }
            userEmailErrorMessage.observe(this@SignupActivity){
                binding.tvEmailError.text = it.asString(this@SignupActivity)
            }
        }
    }

    private fun handleTextChanged(liveData: MutableLiveData<String>, text: Editable?){
        liveData.updateStringData(text.toString())
    }


}