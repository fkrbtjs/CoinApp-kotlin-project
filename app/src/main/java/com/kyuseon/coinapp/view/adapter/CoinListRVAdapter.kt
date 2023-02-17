package com.kyuseon.coinapp.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.kyuseon.coinapp.R
import com.kyuseon.coinapp.db.entity.InterestCoinEntity

class CoinListRVAdapter(val context : Context, val dataSet : List<InterestCoinEntity>)
    :RecyclerView.Adapter<CoinListRVAdapter.ViewHolder>(){

    interface ItemClick {
        fun onClick(view : View , position : Int)
    }
    var itemClick : ItemClick? = null

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val coinName = view.findViewById<TextView>(R.id.coinName)
        val likeBtn = view.findViewById<ImageView>(R.id.likeBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_coin_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.likeBtn.setOnClickListener { v ->
            itemClick?.onClick(v,position)
        }

        holder.coinName.text = dataSet[position].coin_name

        val selected = dataSet[position].selected
        if(selected){
            holder.likeBtn.setImageResource(R.drawable.like_red)
        }else{
            holder.likeBtn.setImageResource(R.drawable.like_grey)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}