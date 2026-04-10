package com.shinoaki.shoppingmall.ui.activity.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shinoaki.shoppingmall.adapter.HotFragmentLeftMenuAdapter
import com.shinoaki.shoppingmall.adapter.HotFragmentRightMenuAdapter
import com.shinoaki.shoppingmall.databinding.FragmentHotBinding
import com.shinoaki.shoppingmall.service.HomeByHotFragmentDataService
import kotlin.random.Random


/**
 * 热点数据 下拉刷新, 上拉加载
 * A simple [Fragment] subclass.
 * Use the [HotFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HotFragment : Fragment() {
    lateinit var binding: FragmentHotBinding
    lateinit var rightAdapter: HotFragmentRightMenuAdapter
    lateinit var leftAdapter: HotFragmentLeftMenuAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HomeByHotFragmentDataService.loadAllProducts(requireContext())
        //缓存更多item
        binding.recyclerViewRight.setItemViewCacheSize(20)
        //固定item尺寸避免重新测量
        binding.recyclerViewRight.setHasFixedSize(true)
        //滑动监听
        binding.recyclerViewRight.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Glide.with(this@HotFragment).pauseRequests() // 滑动时暂停图片加载
                } else {
                    Glide.with(this@HotFragment).resumeRequests() // 停止滑动恢复加载
                }
            }
        })
        //初始化加载数据
        leftAdapter =
            HotFragmentLeftMenuAdapter(HomeByHotFragmentDataService.getHotFragmentLeftMenuData())
        rightAdapter = HotFragmentRightMenuAdapter(
            HomeByHotFragmentDataService.getHotFragmentRightMenuData(
                leftAdapter.items[0].id,
                Random.nextInt(1, 10)
            ).data
        )
        binding.recyclerViewLeft.adapter = leftAdapter
        binding.recyclerViewRight.adapter = rightAdapter
        //左侧菜单点击
        leftAdapter.setOnItemClickListener { item, i ->
            refreshData()
            binding.recyclerViewRight.smoothScrollToPosition(0)
        }
        //下拉刷新 监听
        binding.swipeRefreshLayout.setOnRefreshListener { refreshData() }
    }

    private fun refreshData() {
        rightAdapter.updateData(
            HomeByHotFragmentDataService.getHotFragmentRightMenuData(
                leftAdapter.items[leftAdapter.getSelectedPosition()].id,
                Random.nextInt(1, 10)
            )
        )
        binding.swipeRefreshLayout.isRefreshing = false
    }
}