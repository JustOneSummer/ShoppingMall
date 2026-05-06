package com.shinoaki.shoppingmall.ui.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.shinoaki.shoppingmall.adapter.OrderItemAdapter
import com.shinoaki.shoppingmall.data.LoginDataStoreManager
import com.shinoaki.shoppingmall.databinding.FragmentMineBinding
import com.shinoaki.shoppingmall.db.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * 订单列表
 * A simple [Fragment] subclass.
 * Use the [MineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MineFragment : Fragment() {

    lateinit var binding: FragmentMineBinding
    lateinit var adapter: OrderItemAdapter
    lateinit var db: AppDatabase
    lateinit var userDataStoreManager: LoginDataStoreManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        updateOrder()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = AppDatabase.getInstance(requireContext())
        userDataStoreManager = LoginDataStoreManager(requireContext())
        adapter = OrderItemAdapter()
        binding.recyclerView.adapter = adapter
        //初始化加载数据
        updateOrder()
        //下拉刷新
        binding.smartRefreshLayout.setOnRefreshListener { updateOrder() }
    }
    fun updateOrder() {
        lifecycleScope.launch(Dispatchers.IO) {
            val userName = userDataStoreManager.getUserEmail()
            val list = db.orderDao().getAllCar(userName ?: "")
            withContext(Dispatchers.Main) {
                adapter.updateData(list)
                binding.smartRefreshLayout.finishRefresh(true)
            }
        }
    }
}