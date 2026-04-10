package com.shinoaki.shoppingmall.service

import android.content.Context
import com.shinoaki.shoppingmall.data.LatestFragmentMenuItem
import com.shinoaki.shoppingmall.data.PageResponse
import org.json.JSONArray

class HomeByLatestFragmentDataService {
    companion object {
        private const val FILE_NAME = "products.json"
        private var allProducts: List<LatestFragmentMenuItem> = emptyList()
        public fun getMenuData(
            currentPage: Int,
            pageSize: Int = 10
        ): PageResponse<LatestFragmentMenuItem> {
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

        // 从 assets 加载所有数据
        public fun loadAllProducts(context: Context): List<LatestFragmentMenuItem> {
            val jsonString = context.assets.open(FILE_NAME)
                .bufferedReader().use { it.readText() }

            val jsonArray = JSONArray(jsonString)
            val products = mutableListOf<LatestFragmentMenuItem>()

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                products.add(
                    LatestFragmentMenuItem(
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