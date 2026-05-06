package com.shinoaki.shoppingmall.ui.activity.pass

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.shinoaki.shoppingmall.R
import com.shinoaki.shoppingmall.data.LoginDataStoreManager
import com.shinoaki.shoppingmall.databinding.ActivityChangePasswordBinding
import com.shinoaki.shoppingmall.ui.activity.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangePasswordActivity : AppCompatActivity() {
    lateinit var userDataStoreManager: LoginDataStoreManager
    lateinit var binding: ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userDataStoreManager = LoginDataStoreManager(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.nextButton.setOnClickListener { nextButton(it) }
        binding.topReturn.setOnClickListener {
            finish()
        }
    }

    fun nextButton(v: View) {
        //判定两个密码是否一致
        val pwd1 = binding.textInputLayoutPassword.editText?.text.toString()
        val pwd2 = binding.textInputLayoutPassword2.editText?.text.toString()
        if (pwd1.isEmpty() || pwd2.isEmpty()) {
            Toast.makeText(this, "密码不允许为空", Toast.LENGTH_SHORT).show()
        } else {
            if (pwd1 != pwd2) {
                Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "密码修改成功,返回登录页面重新登录", Toast.LENGTH_SHORT).show()
                lifecycleScope.launch(Dispatchers.IO) {
                    //最终验证 获取email 验证码 密码
                    val email = userDataStoreManager.getUserEmail()
                    //切换至Main线程
                    withContext(Dispatchers.Main) {
                        val intent = Intent(this@ChangePasswordActivity, LoginActivity::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}