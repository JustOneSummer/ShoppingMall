package com.shinoaki.shoppingmall.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.shinoaki.shoppingmall.R
import com.shinoaki.shoppingmall.data.LoginDataStoreManager
import com.shinoaki.shoppingmall.databinding.ActivityDeliveryAddressBinding
import com.shinoaki.shoppingmall.db.AppDatabase
import com.shinoaki.shoppingmall.entity.DeliveryAddressEntity
import com.shinoaki.shoppingmall.utils.DialogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DeliveryAddressActivity : AppCompatActivity() {
    lateinit var binding: ActivityDeliveryAddressBinding
    lateinit var userDataStoreManager: LoginDataStoreManager
    lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDeliveryAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userDataStoreManager = LoginDataStoreManager(this)
        db = AppDatabase.getInstance(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        query()
        //保存
        binding.nextButton.setOnClickListener {
            //获取文本信息
            val name = binding.textInputLayoutName.editText?.text.toString()
            val phone = binding.textInputLayoutPhone.editText?.text.toString()
            val address = binding.textInputLayoutAddress.editText?.text.toString()
            if (name.isBlank() || phone.isBlank() || address.isBlank()) {
                Toast.makeText(this@DeliveryAddressActivity, "请填写完整信息", Toast.LENGTH_SHORT)
                    .show()
            } else {
                lifecycleScope.launch(Dispatchers.IO) {
                    val email = userDataStoreManager.getUserEmail() ?: ""
                    //查询是否有 地址
                    val deliveryAddress = db.deliveryAddressDao().get(email)
                    if (deliveryAddress != null) {
                        db.deliveryAddressDao().update(
                            DeliveryAddressEntity(
                                email,
                                name,
                                phone,
                                address
                            )
                        )
                    } else {
                        db.deliveryAddressDao().add(
                            DeliveryAddressEntity(
                                email,
                                name,
                                phone,
                                address
                            )
                        )
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@DeliveryAddressActivity, "保存成功", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    }
                }
            }
        }
    }

    fun query() {
        lifecycleScope.launch(Dispatchers.IO) {
            val email = userDataStoreManager.getUserEmail() ?: ""
            //查询是否有 地址
            val deliveryAddress = db.deliveryAddressDao().get(email)
            if (deliveryAddress != null) {
                withContext(Dispatchers.Main){
                    binding.textInputLayoutName.editText?.setText(deliveryAddress.name)
                    binding.textInputLayoutPhone.editText?.setText(deliveryAddress.phone)
                    binding.textInputLayoutAddress.editText?.setText(deliveryAddress.address)
                }
            }
        }
    }
}