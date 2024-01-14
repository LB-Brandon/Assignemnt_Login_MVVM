package com.brandon.assignemnt_login_mvvm

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.brandon.assignemnt_login_mvvm.Const.EXTRA_ENTRY_TYPE
import com.brandon.assignemnt_login_mvvm.Const.PREFERENCE_KEY_USERNAME
import com.brandon.assignemnt_login_mvvm.Const.PREFERENCE_KEY_USER_EMAIL
import com.brandon.assignemnt_login_mvvm.Const.PREFERENCE_KEY_USER_PASSWORD
import com.brandon.assignemnt_login_mvvm.Const.SHARED_PREFERENCES_USER_INFO
import com.brandon.assignemnt_login_mvvm.UserInfoViewModel.Companion.updateStringData
import com.brandon.assignemnt_login_mvvm.databinding.ActivitySignupBinding

class UserInfoActivity : AppCompatActivity() {

    val TAG = "Main"

    private val binding: ActivitySignupBinding by lazy {
        ActivitySignupBinding.inflate(layoutInflater)
    }
    private val viewModel: UserInfoViewModel by viewModels()

    private val entryType: UserInfoEntryType by lazy {
        UserInfoEntryType.getEntryType(
            intent?.getIntExtra(EXTRA_ENTRY_TYPE, 0)
        )
    }

    companion object {

        fun newIntent(
            context: Context,
            entryType: UserInfoEntryType,
        ): Intent {
            return Intent(context, UserInfoActivity::class.java).apply {
                putExtra(EXTRA_ENTRY_TYPE, entryType.ordinal)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        observing()
        setUserInfo()
    }

    private fun initViews() {
        with(binding) {
            // 유저 입력 정보 viewModel 전달
            etName.addTextChangedListener {
                viewModel.username.updateStringData(it.toString())
            }
            etEmail.addTextChangedListener {
                viewModel.userEmail.updateStringData(it.toString())
            }
            etEmailProvider.addTextChangedListener {
                viewModel.userEmailProvider.updateStringData(it.toString())
            }
            etPassword.addTextChangedListener {
                viewModel.userPassword.updateStringData(it.toString())
            }
            etPasswordConfirm.addTextChangedListener {
                viewModel.userPasswordConfirm.updateStringData(it.toString())
            }
            // 회원가입 버튼
            binding.btnConfirm.setOnClickListener {
                saveUserInfoToSharedPreferences()
                startActivity(Intent(this@UserInfoActivity, MainActivity::class.java))
            }
        }

    }

    private fun observing() {
        // 스피너 초기화
        viewModel.spinnerData.observe(this) { data ->
            val emailProviders = data.map { getString(it) }
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, emailProviders)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spEmailProvider.adapter = adapter
        }
        // 스피너 선택 이벤트 처리
        binding.spEmailProvider.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    // 스피너 아이템 선택
                    viewModel.spinnerSelectedItemPosition.postValue(position)
                    viewModel.userEmailProvider.value =
                        viewModel.getSpinnerSelectedProvider(position)
                            .asString(this@UserInfoActivity)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
        // 유효성 검사 후 에러 메시지 설정
        viewModel.username.observe(this) {
            viewModel.getMessageValidName(it)
        }
        viewModel.userEmail.observe(this) {
            viewModel.getMessageValidEmail(it)
        }
        viewModel.userEmailProvider.observe(this) {
            val isVisible = binding.etEmailProvider.isVisible
            viewModel.getMessageValidEmailProvider(it, isVisible)
        }
        viewModel.userPassword.observe(this) {
            viewModel.getMessageValidPassword(it)
        }
        viewModel.userPasswordConfirm.observe(this) {
            viewModel.getMessageValidPasswordConfirm(it)
        }

        // 유효성 검사 결과를 UI 에 업데이트
        viewModel.usernameErrorMessage.observe(this) {
            binding.tvNameError.text = it.asString(this)
        }
        viewModel.userEmailErrorMessage.observe(this) {
            binding.tvEmailError.text = it.asString(this)
        }
        viewModel.userEmailProviderErrorMessage.observe(this) {
            val message = if (it.asString(this).isBlank()) {
                viewModel.userEmailErrorMessage.value?.asString(this) ?: ""
            } else {
                it.asString(this)
            }
            binding.tvEmailError.text = message
        }
        viewModel.userPasswordErrorMessage.observe(this) {
            binding.tvPasswordError.text = it.asString(this)
        }
        viewModel.userPasswordConfirmErrorMessage.observe(this) {
            binding.tvPasswordConfirmError.text = it.asString(this)
        }
        // 선택된 스피너 아이템 UI 에 업데이트
        viewModel.spinnerSelectedItemPosition.observe(this) {
            Log.d(TAG, "position: $it")
            binding.etEmailProvider.isVisible = it == 0
            binding.spEmailProvider.setSelection(viewModel.spinnerSelectedItemPosition.value ?: 0)
        }
        // 회원가입 버튼 활성화 업데이트
        viewModel.confirmButtonState.observe(this) {
            Log.d(TAG, "버튼 상태: $it")
            binding.btnConfirm.isEnabled = it
        }

    }

    private fun saveUserInfoToSharedPreferences() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(SHARED_PREFERENCES_USER_INFO, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // 사용자 정보 sharedPreferences 저장
        editor.putString(PREFERENCE_KEY_USERNAME, viewModel.username.value)
        editor.putString(PREFERENCE_KEY_USER_PASSWORD, viewModel.userPassword.value)
        editor.putString(PREFERENCE_KEY_USER_EMAIL, getUserEmail())

        // 변경사항 적용
        editor.apply()
    }

    private fun setUserInfo() {
        if (entryType == UserInfoEntryType.CREATE) return
        else if (entryType == UserInfoEntryType.UPDATE) {
            Log.d(TAG, "setUserInfo called")
            val userInfo = getUserInfoFromSharedPreferences()
            with(binding) {
                etName.setText(userInfo.username)
                etPassword.setText(userInfo.userPassword)
                setEmailWithProvider(userInfo.userEmail)
                // Confirm button 변경
                btnConfirm.text = "회원정보 수정"
            }
        }
    }

    private fun getUserInfoFromSharedPreferences(): UserInfo {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences(SHARED_PREFERENCES_USER_INFO, Context.MODE_PRIVATE)

        // 사용자 정보를 SharedPreferences 에서 가져오기
        val username = sharedPreferences.getString(PREFERENCE_KEY_USERNAME, "")
        val userPassword = sharedPreferences.getString(PREFERENCE_KEY_USER_PASSWORD, "")
        val userEmail = sharedPreferences.getString(PREFERENCE_KEY_USER_EMAIL, "")
        return UserInfo(username ?: "null", userEmail ?: "null", userPassword ?: "null")
    }

    private fun setEmailWithProvider(userEmail: String) {
        with(binding) {
            val (email, provider) = userEmail.split("@")
            Log.d(TAG, "$email, $provider")
            var position = 0
            viewModel.emailProvider.forEachIndexed { index, i ->
                if (provider == getString(i)) {
                    Log.d(TAG, "gacha!! $index")
                    position = index
                }
            }
            // 직접 입력인 경우
            viewModel.spinnerSelectedItemPosition.value = position
            // 데이터 세팅
            if (etEmailProvider.isVisible) {
                etEmailProvider.setText(provider)
            }
            etEmail.setText(email)
        }
    }

    private fun getUserEmail(): String {
        return when (binding.etEmailProvider.visibility) {
            View.VISIBLE -> { // 직접 입력
                with(viewModel) {
                    userEmail.value + "@" + userEmailProvider.value
                }
            }

            else -> { //스피너 입력
                with(viewModel) {
                    userEmail.value + "@" + getSpinnerSelectedProvider(
                        spinnerSelectedItemPosition.value
                    ).asString(this@UserInfoActivity)
                }
            }
        }
    }
}