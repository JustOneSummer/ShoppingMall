package com.shinoaki.shoppingmall.db

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.shinoaki.shoppingmall.dao.CarDao
import com.shinoaki.shoppingmall.dao.DeliveryAddressDao
import com.shinoaki.shoppingmall.dao.OrderDao
import com.shinoaki.shoppingmall.entity.CarEntity
import com.shinoaki.shoppingmall.entity.DeliveryAddressEntity
import com.shinoaki.shoppingmall.entity.OrderEntity

@Database(
    entities = [CarEntity::class, OrderEntity::class, DeliveryAddressEntity::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao

    abstract fun orderDao(): OrderDao

    abstract fun deliveryAddressDao(): DeliveryAddressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shopping_mall_db"
                )
                    //自动清空数据库数据
//                    .fallbackToDestructiveMigration()
                    .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}