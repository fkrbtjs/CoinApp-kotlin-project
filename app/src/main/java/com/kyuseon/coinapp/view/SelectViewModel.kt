package com.kyuseon.coinapp.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.kyuseon.coinapp.dataModel.CurrentPrice
import com.kyuseon.coinapp.dataModel.CurrentPriceResult
import com.kyuseon.coinapp.dataStore.MyDataStore
import com.kyuseon.coinapp.db.entity.InterestCoinEntity
import com.kyuseon.coinapp.repository.DBRepository
import com.kyuseon.coinapp.repository.NetWorkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


class SelectViewModel : ViewModel(){

    private val netWorkRepository = NetWorkRepository()
    private val dbRepository = DBRepository()
    private lateinit var currentPriceResultList : ArrayList<CurrentPriceResult>

    //데이터 변화를 관찰 LiveData
    private val _currentPriceResult = MutableLiveData<List<CurrentPriceResult>>()
    val currentPriceResult : LiveData<List<CurrentPriceResult>>
            get() = _currentPriceResult

    private val _saved = MutableLiveData<String>()
    val save : LiveData<String>
        get() = _saved

    fun getCurrentCoinList() = viewModelScope.launch {

        val result = netWorkRepository.getCurrentCoinList()

        currentPriceResultList = ArrayList()

        for (coin in result.data){

            try{
                val gson = Gson()
                val gsonToJson = gson.toJson(result.data.get(coin.key))
                val gsonFromJson = gson.fromJson(gsonToJson,CurrentPrice::class.java)
                val currentPriceResult = CurrentPriceResult(coin.key,gsonFromJson)

                currentPriceResultList.add(currentPriceResult)

            }catch (e: java.lang.Exception){
                Timber.d(e.toString())
            }
        }
        _currentPriceResult.value = currentPriceResultList
    }

    fun setUpFirstFlag() = viewModelScope.launch {
        MyDataStore().setupFirstData()
    }

    // DB에 데이터 저장
    // Dispatchers : Room database를 사용할때 씀
    fun saveSelectedCoinList(selectCoinList: ArrayList<String>) = viewModelScope.launch(Dispatchers.IO) {
        //1. 전체 코인데이터를 가져옴
        for(coin in currentPriceResultList){
            Timber.d(coin.toString())

            //포함하면 True 포함하지않으면 False
            val selected = selectCoinList.contains(coin.coinName)

            val interestCoinEntity = InterestCoinEntity(
                0,
                coin.coinName,
                coin.coinInfo.opening_price,
                coin.coinInfo.closing_price,
                coin.coinInfo.min_price,
                coin.coinInfo.max_price,
                coin.coinInfo.units_traded,
                coin.coinInfo.acc_trade_value,
                coin.coinInfo.prev_closing_price,
                coin.coinInfo.units_traded_24H,
                coin.coinInfo.acc_trade_value_24H,
                coin.coinInfo.fluctate_24H,
                coin.coinInfo.fluctate_rate_24H,
                selected
            )
            interestCoinEntity.let {
                dbRepository.insertInterestCoinData(it)
            }
        }

        withContext(Dispatchers.Main){
            _saved.value = "done"
        }
        //2. 내가 선택한 코인인인지 아닌지 분

        //3. 저장
    }

}