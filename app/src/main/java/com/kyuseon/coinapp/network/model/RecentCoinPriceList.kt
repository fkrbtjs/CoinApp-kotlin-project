package com.kyuseon.coinapp.network.model

import com.kyuseon.coinapp.dataModel.RecentPriceData

data class RecentCoinPriceList (
    val status : String,
    val data : List<RecentPriceData>
        )
