package com.shinoaki.shoppingmall.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "cart")
data class CarEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    //用户名
    @ColumnInfo(name = "user_name")
    val userName: String,
    //商品id
    @ColumnInfo(name = "product_id")
    val productId: Int,
    // 数量
    @ColumnInfo(name = "quantity")
    val quantity: Int,
    @ColumnInfo(name = "create_at")
    val createdAt: String,
    @ColumnInfo(name = "update_at")
    val updatedAt: String
) {

}