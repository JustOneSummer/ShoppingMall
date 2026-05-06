package com.shinoaki.shoppingmall.dao

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import com.shinoaki.shoppingmall.entity.CarEntity

@Dao
interface CarDao {
    /**
     * 查询购物车全部商品
     */
    @Query("select * from cart where user_name = :userName")
    fun getAllCar(userName: String): List<CarEntity>

    @Query("select * from cart")
    fun getAllCar(): List<CarEntity>

    @Query("select * from cart where user_name = :userName and product_id = :productId")
    fun getMyProduct(userName: String, productId: Int): CarEntity?

    @Insert
    suspend fun addCar(car: CarEntity): Long

    @Update
    suspend fun updateCar(vararg car: CarEntity): Int

    @Query("UPDATE cart SET quantity = :quantity WHERE user_name = :userName and id = :productId")
    fun updateCarQuantity(userName: String, productId: Int, quantity: Int): Int

    @Delete
    fun deleteCar(car: CarEntity)

    @Query("DELETE FROM cart WHERE id = :id")
    fun deleteCarById(id : Int)

    @Query("DELETE FROM cart WHERE user_name = :userEmail")
    fun deleteCarAll(userEmail : String)
}