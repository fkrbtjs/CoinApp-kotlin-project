package com.kyuseon.coinapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kyuseon.coinapp.dataModel.UpDownDataSet
import com.kyuseon.coinapp.db.entity.InterestCoinEntity
import com.kyuseon.coinapp.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private val dbRepository = DBRepository()
    lateinit var selectedCoinList : LiveData<List<InterestCoinEntity>>

    private val _arr15min = MutableLiveData<List<UpDownDataSet>>()
    val arr15min : LiveData<List<UpDownDataSet>>
        get() = _arr15min
    private val _arr30min = MutableLiveData<List<UpDownDataSet>>()
    val arr30min : LiveData<List<UpDownDataSet>>
        get() = _arr30min
    private val _arr45min = MutableLiveData<List<UpDownDataSet>>()
    val arr45min : LiveData<List<UpDownDataSet>>
        get() = _arr45min



    // CoinListFragment

    fun getAllInterestCoinData() = viewModelScope.launch {

        val coinList = dbRepository.getAllInterestCoinData().asLiveData()
        selectedCoinList = coinList
    }

    fun updateInterestCoinData(interestCoinEntity: InterestCoinEntity) = viewModelScope.launch(Dispatchers.IO) {

        if(interestCoinEntity.selected){
            interestCoinEntity.selected = false
        } else {
            interestCoinEntity.selected = true
        }
        dbRepository.updateInterestCoinData(interestCoinEntity)
    }

    // PriceChangeFragment
    // 1. 관심코인리스트를 가져옴
    // 2. 반복문을 통해 하나씩 가져옴
    // 3. 저장된 코인가격 리스트 가져옴
    // 4. 시간마다 어떻게 변경되었는지 알려줌
    fun getAllSelectedCoinData() = viewModelScope.launch(Dispatchers.IO) {

        // 1.
        val selectedCoinList = dbRepository.getAllInterestSelectedCoinData()

        val arr15min = ArrayList<UpDownDataSet>()
        val arr30min = ArrayList<UpDownDataSet>()
        val arr45min = ArrayList<UpDownDataSet>()
        // 2.
        for(data in selectedCoinList) {
            // 3.
            val coinName = data.coin_name
            // 최신값이 가장 마지막 요소이기 때문에 reversed 해줌
            val oneCoinData = dbRepository.getOneSelectedCoinData(coinName).reversed()
            val size = oneCoinData.size

            // 현재와 15n분전 가격을 비교하려면 데이터가 n+1개이상 있어야한다
            if(size > 1){
                //2개
                val changedPrice =oneCoinData[0].price.toDouble() - oneCoinData[1].price.toDouble()
                val upDownDataSet = UpDownDataSet(
                    coinName,
                    changedPrice.toString()
                )
                arr15min.add(upDownDataSet)
            }
            if(size > 2){
                //3개
                val changedPrice =oneCoinData[0].price.toDouble() - oneCoinData[2].price.toDouble()
                val upDownDataSet = UpDownDataSet(
                    coinName,
                    changedPrice.toString()
                )
                arr30min.add(upDownDataSet)
            }
            if(size> 3){
                //4개
                val changedPrice =oneCoinData[0].price.toDouble() - oneCoinData[3].price.toDouble()
                val upDownDataSet = UpDownDataSet(
                    coinName,
                    changedPrice.toString()
                )
                arr45min.add(upDownDataSet)
            }
        }

        //setValue를 background Thread에서 해주면 안되기 때문
        withContext(Dispatchers.Main){
            _arr15min.value = arr15min
            _arr30min.value = arr30min
            _arr45min.value = arr45min
        }

    }
}