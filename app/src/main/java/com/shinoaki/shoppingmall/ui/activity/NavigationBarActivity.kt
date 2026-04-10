package com.shinoaki.shoppingmall.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentTransaction
import com.shinoaki.shoppingmall.R
import com.shinoaki.shoppingmall.databinding.ActivityNavigationBarBinding
import com.shinoaki.shoppingmall.ui.activity.fragment.CarFragment
import com.shinoaki.shoppingmall.ui.activity.fragment.HomeFragment
import com.shinoaki.shoppingmall.ui.activity.fragment.MineFragment
import com.shinoaki.shoppingmall.ui.activity.fragment.MyFragment

class NavigationBarActivity : AppCompatActivity() {
    lateinit var binding: ActivityNavigationBarBinding
    private lateinit var homeFragment: HomeFragment
    private lateinit var carFragment: CarFragment
    private lateinit var mineFragment: MineFragment
    private lateinit var myFragment: MyFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNavigationBarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 沉浸式布局
        WindowCompat.setDecorFitsSystemWindows(window, true)
        // 设置底部内边距适配 注释这个也生效
//        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomNavigationView) { view, insets ->
//            val bottomInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
//            view.setPadding(0, 0, 0, bottomInset)
//            insets
//        }

        binding.bottomNavigationView.setOnItemSelectedListener { it -> onItemSelected(it) }
        binding.bottomNavigationView.selectedItemId = R.id.home
    }

    fun onItemSelected(item: MenuItem): Boolean {
        val manager = supportFragmentManager.beginTransaction()
        hideAllFragment(manager)
        when (item.itemId) {
            R.id.home -> {
                if (::homeFragment.isInitialized) {
                    manager.show(homeFragment)
                } else {
                    homeFragment = HomeFragment()
                    manager.add(R.id.frameLayout, homeFragment)
                }
            }

            R.id.car -> {
                if (::carFragment.isInitialized) {
                    manager.show(carFragment)
                } else {
                    carFragment = CarFragment()
                    manager.add(R.id.frameLayout, carFragment)
                }
            }

            R.id.mine -> {
                if (::mineFragment.isInitialized) {
                    manager.show(mineFragment)
                } else {
                    mineFragment = MineFragment()
                    manager.add(R.id.frameLayout, mineFragment)
                }
            }

            R.id.my -> {
                if (::myFragment.isInitialized) {
                    manager.show(myFragment)
                } else {
                    myFragment = MyFragment()
                    manager.add(R.id.frameLayout, myFragment)
                }
            }
        }
        manager.commit()
        return true;
    }

    fun hideAllFragment(manager: FragmentTransaction) {
        if (::homeFragment.isInitialized) {
            manager.hide(homeFragment)
        }
        if (::carFragment.isInitialized) {
            manager.hide(carFragment)
        }
        if (::mineFragment.isInitialized) {
            manager.hide(mineFragment)
        }
        if (::myFragment.isInitialized) {
            manager.hide(myFragment)
        }
    }

}