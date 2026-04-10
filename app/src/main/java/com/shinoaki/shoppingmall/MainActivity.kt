package com.shinoaki.shoppingmall

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.shinoaki.shoppingmall.data.LoginDataStoreManager
import com.shinoaki.shoppingmall.ui.activity.NavigationBarActivity
import com.shinoaki.shoppingmall.ui.activity.login.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var userDataStoreManager: LoginDataStoreManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        userDataStoreManager = LoginDataStoreManager(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        lifecycleScope.launch(Dispatchers.IO) {
            userDataStoreManager.isLoggedIn()
                .take(1).collect { isLoggedIn ->
                    withContext(Dispatchers.Main) {
                        if (isLoggedIn) {
                            // 已登录，跳转主页
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    NavigationBarActivity::class.java
                                )
                            )
                            finishAffinity()
                        } else {
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                        }
                    }
                }
        }
    }
}