package com.shinoaki.shoppingmall.data

import com.shinoaki.shoppingmall.entity.OrderEntity

data class OrderData(
    val order: OrderEntity,
    val shopping: List<RecommendFragmentRightMenuItem>
)
