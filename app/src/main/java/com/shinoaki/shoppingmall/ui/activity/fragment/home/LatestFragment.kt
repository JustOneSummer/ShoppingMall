package com.shinoaki.shoppingmall.ui.activity.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.shinoaki.shoppingmall.adapter.LatestFragmentMenuAdapter
import com.shinoaki.shoppingmall.databinding.FragmentLatestBinding
import com.shinoaki.shoppingmall.service.HomeByLatestFragmentDataService
import com.shinoaki.shoppingmall.ui.activity.ProductDetailsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * A simple [Fragment] subclass.
 * Use the [LatestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LatestFragment : Fragment() {
    lateinit var binding: FragmentLatestBinding
    lateinit var adapter: LatestFragmentMenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLatestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HomeByLatestFragmentDataService.loadAllProducts(requireContext())
        //缓存更多item
        binding.recyclerView.setItemViewCacheSize(20)
        //固定item尺寸避免重新测量
        binding.recyclerView.setHasFixedSize(true)
        //滑动监听
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Glide.with(this@LatestFragment).pauseRequests() // 滑动时暂停图片加载
                } else {
                    Glide.with(this@LatestFragment).resumeRequests() // 停止滑动恢复加载
                }
            }
        })
        adapter = LatestFragmentMenuAdapter()
        binding.recyclerView.adapter = adapter
        adapter.updateData(HomeByLatestFragmentDataService.getMenuData(1, 10).data)
        //监听拉下上拉
        binding.smartRefreshLayout.setEnableLoadMoreWhenContentNotFull(true)
        binding.smartRefreshLayout.setOnRefreshListener { //刷新数据 协程加载
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    delay(3000)
                    adapter.updateData(HomeByLatestFragmentDataService.getMenuData(2, 10).data)
                    withContext(Dispatchers.Main) {
                        binding.smartRefreshLayout.finishRefresh(true)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        binding.smartRefreshLayout.finishRefresh(false)
                    }
                }
            }
        }
        binding.smartRefreshLayout.setOnLoadMoreListener {
            //加载更多数据
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    delay(6000)
                    adapter.addData(HomeByLatestFragmentDataService.getMenuData(3, 10).data)
                    //切换主线程更新数据
                    withContext(Dispatchers.Main) {
                        binding.smartRefreshLayout.finishLoadMore(true)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        binding.smartRefreshLayout.finishLoadMore(false)
                    }
                }
            }
        }
        //监听点击商品
        adapter.setOnItemClickListener { item, i ->
            //跳转到商品详情页面
            val intent = Intent(requireContext(), ProductDetailsActivity::class.java)
            intent.putExtra("productId", item.id)
            startActivity(intent)
        }
    }
}