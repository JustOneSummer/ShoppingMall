package com.shinoaki.shoppingmall.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.shinoaki.shoppingmall.ui.activity.fragment.home.HotFragment
import com.shinoaki.shoppingmall.ui.activity.fragment.home.LatestFragment
import com.shinoaki.shoppingmall.ui.activity.fragment.home.RecommendFragment

class HomePagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    // 页面标题列表
    private val titles = listOf("推荐", "热门", "最新")

    // 页面 Fragment 列表

    private lateinit var recommendFragment: RecommendFragment
    private lateinit var hotFragment: HotFragment
    private lateinit var latestFragment: LatestFragment

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                if (::recommendFragment.isInitialized) {
                    recommendFragment
                } else {
                    recommendFragment = RecommendFragment()
                    recommendFragment
                }
            }

            1 -> {
                if (::hotFragment.isInitialized) {
                    hotFragment
                } else {
                    hotFragment = HotFragment()
                    hotFragment
                }
            }

            else -> {
                if (::latestFragment.isInitialized) {
                    latestFragment
                } else {
                    latestFragment = LatestFragment()
                    latestFragment
                }
            }
        }
    }

    fun getPageTitle(position: Int): String = titles[position]
}