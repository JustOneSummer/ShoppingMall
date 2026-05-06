package com.shinoaki.shoppingmall.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "order_items")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    /**
     * 购买的用户名
     */
    @ColumnInfo(name = "user_email")
    val userEmail: String,
    /**
     * 订单总价
     */
    @ColumnInfo(name = "order_price")
    val orderPrice: String,
    /**
     * 订单时间
     */
    @ColumnInfo(name = "order_time")
    val orderTime: String,
    /**
     * 订单状态 0 未付款 1取消付款 2 已付款 3申请售后
     */
    @ColumnInfo(name = "order_status")
    val orderStatus: Int,
    /**
     * 商品信息json
     */
    @ColumnInfo(name = "product_json")
    val productJson: String,
    /**
     * 配送地址json
     */
    @ColumnInfo(name = "delivery_address_json")
    val deliveryAddressJson: String
)

