package com.kyuseon.coinapp.db

import android.content.Context
import androidx.room.*
import com.kyuseon.coinapp.db.dao.InterestCoinDao
import com.kyuseon.coinapp.db.dao.SelectedCoinPriceDAO
import com.kyuseon.coinapp.db.entity.DateConverters
import com.kyuseon.coinapp.db.entity.InterestCoinEntity
import com.kyuseon.coinapp.db.entity.SelectedCoinPriceEntity

@Database(entities = [InterestCoinEntity::class, SelectedCoinPriceEntity::class], version = 3)
@TypeConverters(DateConverters::class)
abstract class CoinPriceDatabase : RoomDatabase() {

    abstract fun interestCoinDAO() : InterestCoinDao
    abstract fun selectedCoinDAO() : SelectedCoinPriceDAO

    companion object {

        @Volatile
        private var INSTANCE : CoinPriceDatabase? = null

        fun getDatabase(
            context : Context
        ) : CoinPriceDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CoinPriceDatabase::class.java,
                    "coin_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }


        }

    }


}