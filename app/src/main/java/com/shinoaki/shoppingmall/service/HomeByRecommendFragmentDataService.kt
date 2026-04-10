package com.shinoaki.shoppingmall.service

import android.content.Context
import com.shinoaki.shoppingmall.data.RecommendFragmentLeftMenuItem
import com.shinoaki.shoppingmall.data.RecommendFragmentRightMenuItem
import org.json.JSONArray
import kotlin.random.Random

class HomeByRecommendFragmentDataService {
    companion object {
        private const val FILE_NAME = "products.json"
        private var allProducts: List<RecommendFragmentRightMenuItem> = emptyList()
        public fun getRecommendFragmentRightMenuData(id: Int): List<RecommendFragmentRightMenuItem> {
            if (allProducts.isEmpty()) {
                return emptyList()
            }

            // 生成 1-100 的随机数
            val randomValue = Random.nextInt(1, 101)

            return when {
                // 1-30：返回 0 条数据（30% 概率）
                randomValue <= 30 -> emptyList()

                // 31-100：返回 1-50 条随机数据（70% 概率）
                else -> {
                    val maxCount = minOf(50, allProducts.size)
                    val count = Random.nextInt(1, maxCount + 1)
                    allProducts.shuffled().take(count)
                }
            }
        }

        public fun findById(id: Int): RecommendFragmentRightMenuItem? {
            return allProducts.find { it.id == id }
        }

        public fun getRecommendFragmentLeftMenuData(): List<RecommendFragmentLeftMenuItem> {
            return listOf(
                RecommendFragmentLeftMenuItem(
                    1,
                    "食品",
                    "https://qlogo4.store.qq.com/qzone/2622749113/2622749113/100"
                ),
                RecommendFragmentLeftMenuItem(
                    2,
                    "饮料",
                    "https://shinoaki.com/images/avatar.jpg"
                ),
                RecommendFragmentLeftMenuItem(
                    3,
                    "服装",
                    "https://shinoaki.com/images/avatar.jpg"
                ),
                RecommendFragmentLeftMenuItem(
                    4,
                    "电器",
                    "https://shinoaki.com/images/avatar.jpg"
                ),
                RecommendFragmentLeftMenuItem(
                    5,
                    "日用品",
                    "https://shinoaki.com/images/avatar.jpg"
                ),
                RecommendFragmentLeftMenuItem(
                    6,
                    "化妆品",
                    "https://shinoaki.com/images/avatar.jpg"
                )
            )
        }

        // 从 assets 加载所有数据
        public fun loadAllProducts(context: Context): List<RecommendFragmentRightMenuItem> {
            val jsonString = context.assets.open(FILE_NAME)
                .bufferedReader().use { it.readText() }

            val jsonArray = JSONArray(jsonString)
            val products = mutableListOf<RecommendFragmentRightMenuItem>()

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                products.add(
                    RecommendFragmentRightMenuItem(
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