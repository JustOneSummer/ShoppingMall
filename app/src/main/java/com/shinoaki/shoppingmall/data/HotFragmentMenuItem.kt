package com.shinoaki.shoppingmall.data

data class HotFragmentLeftMenuItem(
    val id: Int,
    val title: String,
    val iconUrl: String,
    var isSelected: Boolean = false
)

data class HotFragmentRightMenuItem(
    val id: Int,
    val title: String,
    val iconUrl: String,
    val monthlySales: String,
    var label: String,
    var price: String,
    var detail: String
)