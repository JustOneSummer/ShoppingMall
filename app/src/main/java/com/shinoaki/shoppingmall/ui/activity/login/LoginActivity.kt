package com.shinoaki.shoppingmall.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.shinoaki.shoppingmall.R
import com.shinoaki.shoppingmall.data.LoginDataStoreManager
import com.shinoaki.shoppingmall.databinding.ActivityLoginBinding
import com.shinoaki.shoppingmall.ui.activity.NavigationBarActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    lateinit var userDataStoreManager: LoginDataStoreManager
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userDataStoreManager = LoginDataStoreManager(this)
        var email = intent.getStringExtra("email") ?: ""
        binding.textInputLayoutEmail.editText?.setText(email)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //跳转注册页面
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        //密码密码
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
        binding.loginButton.setOnClickListener {
            //保存登录状态
            email = binding.textInputLayoutEmail.editText?.text.toString()
            if (email.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    userDataStoreManager.setLoginStatus(email, isLoggedIn = true)
                }
                startActivity(Intent(this, NavigationBarActivity::class.java))
                finishAffinity()
            } else {
                Toast.makeText(this, "请输入邮箱", Toast.LENGTH_SHORT).show()
            }
        }
    }
}