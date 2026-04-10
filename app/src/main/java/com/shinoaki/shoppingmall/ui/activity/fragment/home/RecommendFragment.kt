package com.shinoaki.shoppingmall.ui.activity.fragment.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shinoaki.shoppingmall.adapter.RecommendFragmentLeftMenuAdapter
import com.shinoaki.shoppingmall.adapter.RecommendFragmentRightMenuAdapter
import com.shinoaki.shoppingmall.databinding.FragmentRecommendBinding
import com.shinoaki.shoppingmall.service.HomeByRecommendFragmentDataService
import com.shinoaki.shoppingmall.ui.activity.ProductDetailsActivity


/**
 * 普通的列表 一次性加载全部数据
 * A simple [Fragment] subclass.
 * Use the [RecommendFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecommendFragment : Fragment() {

    lateinit var binding: FragmentRecommendBinding
    lateinit var adapterLeft: RecommendFragmentLeftMenuAdapter
    lateinit var adapterRight: RecommendFragmentRightMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecommendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //缓存更多item
        binding.recyclerViewRight.setItemViewCacheSize(20)
        //固定item尺寸避免重新测量
        binding.recyclerViewRight.setHasFixedSize(true)
        //滑动监听
        binding.recyclerViewRight.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    Glide.with(this@RecommendFragment).pauseRequests() // 滑动时暂停图片加载
                } else {
                    Glide.with(this@RecommendFragment).resumeRequests() // 停止滑动恢复加载
                }
            }
        })

        HomeByRecommendFragmentDataService.loadAllProducts(requireContext())
        adapterLeft = RecommendFragmentLeftMenuAdapter(
            HomeByRecommendFragmentDataService.getRecommendFragmentLeftMenuData()
        )
        binding.recyclerViewLeft.adapter = adapterLeft
        adapterRight = RecommendFragmentRightMenuAdapter()
        binding.recyclerViewRight.adapter = adapterRight
        //监听点击事件
        adapterLeft.setOnItemClickListener { item, i ->
            adapterRight.updateData(
                HomeByRecommendFragmentDataService.getRecommendFragmentRightMenuData(
                    item.id
                )
            )
            binding.recyclerViewRight.smoothScrollToPosition(0)
        }
        //加载数据
        adapterRight.updateData(
            HomeByRecommendFragmentDataService.getRecommendFragmentRightMenuData(
                adapterLeft.items[0].id
            )
        )
        binding.recyclerViewRight.smoothScrollToPosition(0)
        //监听点击商品
        adapterRight.setOnItemClickListener { item, i ->
            //跳转到商品详情页面
            val intent = Intent(requireContext(), ProductDetailsActivity::class.java)
            intent.putExtra("productId", item.id)
            startActivity(intent)
        }
    }


}