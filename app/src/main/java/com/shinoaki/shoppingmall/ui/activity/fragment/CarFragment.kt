package com.shinoaki.shoppingmall.ui.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.shinoaki.shoppingmall.adapter.ProductItemAdapter
import com.shinoaki.shoppingmall.data.LoginDataStoreManager
import com.shinoaki.shoppingmall.data.ProductData
import com.shinoaki.shoppingmall.databinding.FragmentCarBinding
import com.shinoaki.shoppingmall.db.AppDatabase
import com.shinoaki.shoppingmall.entity.OrderEntity
import com.shinoaki.shoppingmall.service.HomeByRecommendFragmentDataService
import com.shinoaki.shoppingmall.utils.DateUtils
import com.shinoaki.shoppingmall.utils.DialogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.suspendCoroutine
import kotlin.coroutines.resume
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.math.BigDecimal
import java.util.Locale


/**
 * A simple [Fragment] subclass.
 * Use the [CarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CarFragment : Fragment() {
    lateinit var binding: FragmentCarBinding
    lateinit var adapter: ProductItemAdapter
    lateinit var userDataStoreManager: LoginDataStoreManager
    lateinit var db: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        query()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDataStoreManager = LoginDataStoreManager(requireContext())
        db = AppDatabase.getInstance(requireContext())
        //查询用户的购物车数据 以及事件回调
        adapter = ProductItemAdapter(onQuantityChange = { productData, quantity ->
            updateCarQuantity(productData, quantity)
        }, onCheckBoxChange = { productData, isChecked ->
            updateCar()
        })
        binding.recyclerView.adapter = adapter
        query()
        binding.smartRefreshLayout.setOnRefreshListener {
            query()
        }
        //全选监听
        binding.cbSelectAll.setOnCheckedChangeListener { buttonView, isChecked ->
            //禁用 加减和注解修改按钮
            adapter.selectAll(isChecked)
            updateCar()
        }
        //结算
        binding.carryVelocity.setOnClickListener { view ->
            if (adapter.getProductData().isEmpty()) {
                Toast.makeText(requireContext(), "购物车为空", Toast.LENGTH_SHORT).show()
            } else {
                //价格判断
                if (binding.totalPrice.text.toString().toBigDecimal() <= BigDecimal.ZERO) {
                    Toast.makeText(requireContext(), "购物车为空", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                lifecycleScope.launch {
                    val userEmail = userDataStoreManager.getUserEmail() ?: ""

                    // 获取收货地址
                    val deliveryAddress = withContext(Dispatchers.IO) {
                        db.deliveryAddressDao().get(userEmail)
                    }

                    if (deliveryAddress == null) {
                        Toast.makeText(requireContext(), "请先添加收货地址", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    // 显示确认对话框并等待结果
                    val confirmed = suspendCancellableCoroutine { continuation ->
                        DialogUtil.showConfirmDialog(requireContext(), "提示", "确定结算吗") {
                            continuation.resume(true)
                        }
                    }

                    if (!confirmed) {
                        return@launch
                    }

                    // 处理订单
                    withContext(Dispatchers.IO) {
                        val json = adapter.getProductData()
                            .filter { it.checkStatus }
                            .map { it.shopping }

                        val carInfo = adapter.getProductData()
                            .filter { it.checkStatus }
                            .map { it.car }

                        val order = OrderEntity(
                            userEmail = userEmail,
                            orderTime = DateUtils.getCurrentDate(),
                            orderStatus = 2,
                            orderPrice = binding.totalPrice.text.toString(),
                            productJson = Json.encodeToString(json),
                            deliveryAddressJson = Json.encodeToString(deliveryAddress)
                        )

                        db.orderDao().addCar(order)
                        carInfo.forEach { db.carDao().deleteCarById(it.id) }
                    }

                    // 更新UI
                    binding.totalPrice.text = "0.0"
                    binding.cbSelectAll.isChecked = false
                    query()
                    Toast.makeText(requireContext(), "下单成功", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    fun updateCar() {
        var productMoney = BigDecimal.ZERO
        for (it in adapter.getProductData()) {
            if (it.checkStatus) {
                val price = BigDecimal(it.shopping.price)
                val quantity = BigDecimal(it.car.quantity)
                productMoney = productMoney.add(price.multiply(quantity))
            }
        }
        // 价格显示 小数点显示最多 2位
        binding.totalPrice.text =
            String.format(Locale.getDefault(), "%.2f", productMoney)

    }

    fun updateCarQuantity(product: ProductData, quantity: Int) {
        //禁用 加减和注解修改按钮
        adapter.quantityButtonStatus(false)
        lifecycleScope.launch(Dispatchers.Main) {
            val userEmail = userDataStoreManager.getUserEmail()
            //判定上下限 不超过50 小于等于0 询问是否删除
            if (quantity > 50) {
                Toast.makeText(requireContext(), "商品数量不能超过50", Toast.LENGTH_SHORT).show()
            } else if (quantity <= 0) {
                DialogUtil.showConfirmDialog(requireContext(), "提示", "确定删除该商品吗") {
                    lifecycleScope.launch(Dispatchers.IO) {
                        db.carDao().deleteCar(product.car)
                    }
                    query()
                }
            } else {
                withContext(Dispatchers.IO) {
                    db.carDao().updateCarQuantity(userEmail ?: "", product.car.id, quantity)
                }
                query()
            }
        }
    }


    fun query() {
        lifecycleScope.launch(Dispatchers.IO) {
            val userEmail = userDataStoreManager.getUserEmail()
            val carList = db.carDao().getAllCar(userEmail ?: "")
            val list: MutableList<ProductData> = mutableListOf()
            for (car in carList) {
                val sp = HomeByRecommendFragmentDataService.findById(car.productId)
                if (sp != null) {
                    list.add(ProductData(car, sp))
                }
            }
            withContext(Dispatchers.Main) {
                adapter.updateData(list)
                binding.smartRefreshLayout.finishRefresh(true)
            }
            updateCar()
        }
    }
}