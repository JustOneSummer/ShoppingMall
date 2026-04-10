package com.shinoaki.shoppingmall.service

import android.content.Context
import com.shinoaki.shoppingmall.data.HotFragmentLeftMenuItem
import com.shinoaki.shoppingmall.data.HotFragmentRightMenuItem
import com.shinoaki.shoppingmall.data.PageResponse
import com.shinoaki.shoppingmall.data.RecommendFragmentLeftMenuItem
import com.shinoaki.shoppingmall.data.RecommendFragmentRightMenuItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.time.delay
import org.json.JSONArray
import java.time.Duration
import kotlin.random.Random
import kotlin.time.DurationUnit

class HomeByHotFragmentDataService {
    companion object {
        private const val FILE_NAME = "products.json"
        private var allProducts: List<HotFragmentRightMenuItem> = emptyList()
        public fun getHotFragmentRightMenuData(
            id: Int,
            currentPage: Int,
            pageSize: Int = 10
        ): PageResponse<HotFragmentRightMenuItem> {
            if (allProducts.isEmpty()) {
                return PageResponse(
                    data = emptyList(),
                    page = currentPage,
                    pageSize = pageSize,
                    totalCount = 0,
                    hasMore = false
                )
            }

            // 计算分页的起止位置
            val startIndex = (currentPage - 1) * pageSize
            val endIndex = minOf(startIndex + pageSize, allProducts.size)

            // 判断是否还有更多数据
            val hasMore = endIndex < allProducts.size

            // 获取当前页的数据
            val pageData = if (startIndex < allProducts.size) {
                allProducts.subList(startIndex, endIndex)
            } else {
                emptyList()
            }

            return PageResponse(
                data = pageData,
                page = currentPage,
                pageSize = pageSize,
                totalCount = allProducts.size,
                hasMore = hasMore
            )
        }

        public fun getHotFragmentLeftMenuData(): List<HotFragmentLeftMenuItem> {
            return listOf(
                HotFragmentLeftMenuItem(
                    1,
                    "热点2",
                    "https://qlogo4.store.qq.com/qzone/2622749113/2622749113/100"
                ),
                HotFragmentLeftMenuItem(
                    2,
                    "饮料2",
                    "https://shinoaki.com/images/avatar.jpg"
                ),
                HotFragmentLeftMenuItem(
                    3,
                    "服装2",
                    "https://shinoaki.com/images/avatar.jpg"
                ),
                HotFragmentLeftMenuItem(
                    4,
                    "电器2",
                    "https://shinoaki.com/images/avatar.jpg"
                ),
                HotFragmentLeftMenuItem(
                    5,
                    "日用品2",
                    "https://shinoaki.com/images/avatar.jpg"
                ),
                HotFragmentLeftMenuItem(
                    6,
                    "化妆品2",
                    "https://shinoaki.com/images/avatar.jpg"
                )
            )
        }

        // 从 assets 加载所有数据
        public fun loadAllProducts(context: Context): List<HotFragmentRightMenuItem> {
            val jsonString = context.assets.open(FILE_NAME)
                .bufferedReader().use { it.readText() }

            val jsonArray = JSONArray(jsonString)
            val products = mutableListOf<HotFragmentRightMenuItem>()

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                products.add(
                    HotFragmentRightMenuItem(
                        id = obj.getInt("id"),
                        title = obj.getString("title"),
                        iconUrl = obj.getString("iconUrl"),
                        monthlySales = obj.getString("monthlySales"),
                        label = obj.getString("label"),
                        price = obj.getString("price"),
                        detail = obj.getString("detail")
                    )
                )
            }
            allProducts = products
            return products
        }
    }
}