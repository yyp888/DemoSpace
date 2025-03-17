package com.yyp.cryptodemo.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.yyp.cryptodemo.R
import com.yyp.cryptodemo.bean.CurrencyBalanceBean
import com.yyp.cryptodemo.databinding.ItemHolderCurrencyBinding

/**
 * item适配器
 */
class CurrencyListAdapter : RecyclerView.Adapter<CurrencyListAdapter.CurrencyHolder>() {

    private val dataList: ArrayList<CurrencyBalanceBean> = arrayListOf()

    /**
     * 刷新数据
     */
    fun setData(data: List<CurrencyBalanceBean>?) {
        data?.let {
            dataList.clear()
            dataList.addAll(data)
            if (dataList.isNotEmpty()) {
                notifyItemRangeChanged(0, dataList.size)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyHolder {
        // 加载布局文件
        val view: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_holder_currency, parent, false)
        return CurrencyHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
        val item = dataList[position]
        holder.binding.apply {
            //展示数据
            Glide.with(holder.itemView.context)
                .load(item.currencyLogoUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .into(ivCoin)
            tvCoin.text = item.currencyFullName
            tvAmount.text = "${item.amount} ${item.currencyName}"
            tvBalanceDollar.text = "$ ${item.balance}"
        }
    }

    // 返回数据集合的大小
    override fun getItemCount(): Int {
        return dataList.size
    }

    class CurrencyHolder(itemView: View) : ViewHolder(itemView) {
        val binding: ItemHolderCurrencyBinding = ItemHolderCurrencyBinding.bind(itemView)
    }
}