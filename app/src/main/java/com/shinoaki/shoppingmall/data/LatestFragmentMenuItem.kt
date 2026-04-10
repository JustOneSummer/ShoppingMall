package com.shinoaki.shoppingmall.data

data class LatestFragmentMenuItem(
    val id: Int,
    val title: String,
    val iconUrl: String,
    val monthlySales: String,
    var label: String,
    var price: String,
    var detail: String
){

}
