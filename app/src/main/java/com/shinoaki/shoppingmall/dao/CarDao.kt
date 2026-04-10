package com.shinoaki.shoppingmall.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.shinoaki.shoppingmall.entity.CarEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    /**
     * 查询购物车全部商品
     */
    @Query("select * from cart where user_name = :userName")
    fun getAllCar(userName: String): Flow<CarEntity>

    @Query("select * from cart where user_name = :userName and product_id = :productId")
    fun getMyProduct(userName: String, productId: Int): CarEntity?

    @Insert
    suspend fun addCar(car: CarEntity): Long

    @Update
    suspend fun updateCar(vararg car: CarEntity): Int
}