package com.shinoaki.shoppingmall.ui.activity.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shinoaki.shoppingmall.R
import com.shinoaki.shoppingmall.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgotPasswordBinding
    var buttonStatus: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.nextButton.setOnClickListener { nextButton(it) }

    }

    fun nextButton(v: View) {
        when (buttonStatus) {
            1 -> {
                if (binding.textInputLayoutEmail.editText?.text.toString().isEmpty()) {
                    Toast.makeText(this, "请输入邮箱", Toast.LENGTH_SHORT).show()
                } else {
                    buttonStatus = 2
                    binding.textInputLayoutEmail.visibility = View.GONE
                    binding.textInputLayoutValidationCode.visibility = View.VISIBLE
                }
            }

            2 -> {
                if (binding.textInputLayoutValidationCode.editText?.text.toString().isEmpty()) {
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_SHORT).show()
                } else {
                    //验证码验证
                    if (binding.textInputLayoutValidationCode.editText?.text.toString() != "123456") {
                        Toast.makeText(this, "验证码错误", Toast.LENGTH_SHORT).show()
                    } else {
                        //隐藏
                        binding.textInputLayoutValidationCode.visibility = View.GONE
                        //显示
                        binding.textInputLayoutPassword.visibility = View.VISIBLE
                        binding.textInputLayoutPassword2.visibility = View.VISIBLE
                        binding.nextButton.text = "提交"
                        buttonStatus = 3
                    }
                }
            }

            3 -> {
                //判定两个密码是否一致
                val pwd1 = binding.textInputLayoutPassword.editText?.text.toString()
                val pwd2 = binding.textInputLayoutPassword2.editText?.text.toString()
                if (pwd1.isEmpty() || pwd2.isEmpty()) {
                    Toast.makeText(this, "密码不允许为空", Toast.LENGTH_SHORT).show()
                } else {
                    if (pwd1 != pwd2) {
                        Toast.makeText(this, "密码不一致", Toast.LENGTH_SHORT).show()
                    } else {
                        //最终验证 获取email 验证码 密码
                        val email = binding.textInputLayoutEmail.editText?.text.toString()
                        val code = binding.textInputLayoutValidationCode.editText?.text.toString()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}