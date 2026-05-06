package com.shinoaki.shoppingmall.data

import kotlinx.serialization.Serializable

@Serializable
data class RecommendFragmentLeftMenuItem(
    val id: Int,
    val title: String,
    val iconUrl: String,
    var isSelected: Boolean = false
)

@Serializable
data class RecommendFragmentRightMenuItem(
    val id: Int,
    val title: String,
    val iconUrl: String,
    val monthlySales: String,
    var label: String,
    var price: String,
    var detail: String
)