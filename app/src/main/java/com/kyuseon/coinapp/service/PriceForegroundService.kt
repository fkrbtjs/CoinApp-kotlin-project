package com.kyuseon.coinapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.gson.Gson
import com.kyuseon.coinapp.R
import com.kyuseon.coinapp.dataModel.CurrentPrice
import com.kyuseon.coinapp.dataModel.CurrentPriceResult
import com.kyuseon.coinapp.repository.NetWorkRepository
import com.kyuseon.coinapp.view.main.MainActivity
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class PriceForegroundService : Service() {

    private val netWorkRepository = NetWorkRepository()

    private val NOTIFICATION_ID = 10000

    lateinit var job : Job

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            "START" -> {
                Toast.makeText(this,"알림기능을 시작합니다",Toast.LENGTH_SHORT).show()
                job = CoroutineScope(Dispatchers.IO).launch {
                    while (true){
                        startForeground(NOTIFICATION_ID , makeNotification())
                        delay(3000)
                    }
                }
            }
            "STOP" -> {
                // ForegroundService가 안켜져있을때 예외처리
                try{
                    Toast.makeText(this,"알림기능을 종료합니다",Toast.LENGTH_SHORT).show()
                    job.cancel()
                    stopForeground(true)
                    stopSelf()
                }catch (e : java.lang.Exception){
                    Toast.makeText(this,"알림기능이 실행되지 않았습니다",Toast.LENGTH_SHORT).show()
                }
            }
        }

        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    suspend fun makeNotification() : Notification {

        val result = getAllCoinList()
        val randomNum = Random().nextInt(result.size)
        val title = result[randomNum].coinName
        val content = result[randomNum].coinInfo.fluctate_24H

        // ForegroundService 실행 코드
        val intent = Intent(this,MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent : PendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val builder = NotificationCompat.Builder(this,"CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_baseline_access_alarms_24)
            .setContentTitle("코인이름 : $title")
            .setContentText("변동가격 : $content")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "name"
            val descriptionText = "descriptionText"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel("CHANNEL_ID",name,importance).apply {
                description = descriptionText
            }
            val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        return builder.build()
    }

    // 전체 코인 정보를 가져옴
    suspend fun getAllCoinList() : ArrayList<CurrentPriceResult>{
        val result = netWorkRepository.getCurrentCoinList()
        val currentPriceResultList = ArrayList<CurrentPriceResult>()


        for (coin in result.data){

            try{
                val gson = Gson()
                val gsonToJson = gson.toJson(result.data.get(coin.key))
                val gsonFromJson = gson.fromJson(gsonToJson, CurrentPrice::class.java)
                val currentPriceResult = CurrentPriceResult(coin.key,gsonFromJson)

                currentPriceResultList.add(currentPriceResult)

            }catch (e: java.lang.Exception){
                Timber.d(e.toString())
            }
        }
        return currentPriceResultList
    }
}