package com.shinoaki.shoppingmall.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.shinoaki.shoppingmall.entity.DeliveryAddressEntity

@Dao
interface DeliveryAddressDao {

    @Query("select * from `delivery_address` where email = :email")
    fun get(email: String): DeliveryAddressEntity?

    @Insert
    suspend fun add(data: DeliveryAddressEntity): Long

    @Update
    suspend fun update(vararg data: DeliveryAddressEntity): Int
}