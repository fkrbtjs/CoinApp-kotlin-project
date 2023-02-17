package com.kyuseon.coinapp.repository

import com.kyuseon.coinapp.network.Api
import com.kyuseon.coinapp.network.RetrofitInstance

class NetWorkRepository {

    private val client = RetrofitInstance.getInstance().create(Api::class.java)

    suspend fun getCurrentCoinList() = client.getCurrentCoinList()

    suspend fun getInterestCoinPriceData(coin : String) = client.getRecentCoinPrice(coin)

}