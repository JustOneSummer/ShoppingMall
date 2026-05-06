package com.shinoaki.shoppingmall.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.shinoaki.shoppingmall.entity.OrderEntity

@Dao
interface OrderDao {

    /**
     * 查询全部订单
     */
    @Query("select * from `order_items` where user_email = :userName order by order_time desc")
    fun getAllCar(userName: String): List<OrderEntity>

    @Insert
    suspend fun addCar(car: OrderEntity): Long

    @Update
    suspend fun updateCar(vararg car: OrderEntity): Int
}