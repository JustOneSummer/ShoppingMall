package com.shinoaki.shoppingmall.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.shinoaki.shoppingmall.R
import com.shinoaki.shoppingmall.dao.CarDao
import com.shinoaki.shoppingmall.data.LoginDataStoreManager
import com.shinoaki.shoppingmall.databinding.ActivityProductDetailsBinding
import com.shinoaki.shoppingmall.db.AppDatabase
import com.shinoaki.shoppingmall.entity.CarEntity
import com.shinoaki.shoppingmall.service.HomeByRecommendFragmentDataService
import com.shinoaki.shoppingmall.utils.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityProductDetailsBinding
    var productId: Int = 0
    lateinit var car: CarDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        productId = intent.getIntExtra("productId", 1)
        bind(productId)
        binding.toolbar.setOnClickListener {
            finish()
        }
        car = AppDatabase.getInstance(this).carDao()
        //点击加入购物车
        binding.carryVelocity.setOnClickListener {
            binding.carryVelocity.isEnabled = false
            //显式指定 IO 线程 否则还是默认主线程
            lifecycleScope.launch(Dispatchers.IO) {
                //先查询当前商品数量
                val context = this@ProductDetailsActivity
                try {
                    val dataStore = LoginDataStoreManager(context)
                    val product = car.getMyProduct(dataStore.getUserEmail() ?: "", productId)
                    if (product != null) {
                        //数量+1
                        car.updateCar(
                            CarEntity(
                                product.id,
                                product.userName,
                                product.productId,
                                product.quantity + 1,
                                product.createdAt,
                                DateUtils.getCurrentDate()
                            )
                        )
                    } else {
                        //添加商品
                        car.addCar(
                            CarEntity(
                                id = 0,
                                userName = dataStore.getUserEmail() ?: "",
                                productId = productId,
                                quantity = 1,
                                createdAt = DateUtils.getCurrentDate(),
                                updatedAt = DateUtils.getCurrentDate()
                            )
                        )
                    }
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show()
                        binding.carryVelocity.isEnabled = true
                        finish()
                    }
                } catch (e: Exception) {
                    Log.e("ProductDetailsActivity", "添加购物车异常: ${e.message}", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show()
                        binding.carryVelocity.isEnabled = true
                    }
                }
            }
        }
    }

    fun bind(productId: Int) {
        val item = HomeByRecommendFragmentDataService.findById(productId)
        Glide.with(this)
            .load(item?.iconUrl)           // 网络图片URL
            .placeholder(R.drawable.acc)  // 加载中占位图
            .error(R.drawable.gwc)         // 加载失败占位图
//                .circleCrop()                       // 圆形裁剪（可选）
            .into(binding.imageViewBanner)

        binding.price.text = item?.price
        binding.productTitle.text = item?.title
        binding.productDetails.text = item?.detail
    }
}