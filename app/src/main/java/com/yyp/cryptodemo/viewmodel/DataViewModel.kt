package com.yyp.cryptodemo.viewmodel

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.yyp.cryptodemo.bean.CurrencyBalanceBean
import com.yyp.cryptodemo.bean.CurrencyBean
import com.yyp.cryptodemo.bean.CurrencyResponse
import com.yyp.cryptodemo.bean.LiveRateBean
import com.yyp.cryptodemo.bean.LiveRateResponse
import com.yyp.cryptodemo.bean.WalletBalanceBean
import com.yyp.cryptodemo.bean.WalletBalanceResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.math.BigDecimal
import java.math.RoundingMode


class DataViewModel : ViewModel() {

    val currencyListLiveData = MutableLiveData<List<CurrencyBalanceBean>?>() //列表数据
    val walletBalanceLiveData = MutableLiveData<Double>() //钱包总余额 单位美元

    /**
     * 获取全部数据
     */
    fun getAllData(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val currencyJob = async { getCurrencyData(context) }
            val liveRateJob = async { getLiveRateData(context) }
            val walletBalanceJob = async { getWalletBalanceData(context) }
            val jobList = awaitAll(
                currencyJob,
                liveRateJob,
                walletBalanceJob
            )
            var currencyList: List<CurrencyBean>? = null
            var liveRateList: List<LiveRateBean>? = null
            var walletBalanceList: List<WalletBalanceBean>? = null
            jobList.forEach {
                when (it) {
                    is CurrencyResponse -> {
                        currencyList = it.currencies
                    }

                    is LiveRateResponse -> {
                        liveRateList = it.tiers
                    }

                    is WalletBalanceResponse -> {
                        walletBalanceList = it.wallet
                    }
                }
            }

            var totalBalance = 0.0
            val list = arrayListOf<CurrencyBalanceBean>()
            currencyList?.forEach { currency ->
                //找到对应余额
                val walletBalance = walletBalanceList?.find {
                    TextUtils.equals(it.currency, currency.symbol)
                }
                walletBalance?.amount.let { amount ->
                    //拿到货币对应美元的汇率
                    val liveRate = liveRateList?.find {
                        TextUtils.equals(it.from_currency, currency.symbol)
                                && TextUtils.equals(it.to_currency, "USD")
                    }?.rates?.first()?.rate ?: 0.0
                    val balance = BigDecimal(liveRate.times(amount ?: 0.0))
                        .setScale(2, RoundingMode.HALF_EVEN)
                        .toDouble()
                    totalBalance += balance
                    val currencyBalanceBean = CurrencyBalanceBean(
                        currency.coin_id,
                        currency.colorful_image_url,
                        currency.name,
                        currency.symbol,
                        amount,
                        balance
                    )
                    list.add(currencyBalanceBean)
                }
            }
            withContext(Dispatchers.Main) {
                //通知页面刷新数据
                walletBalanceLiveData.value = totalBalance
                currencyListLiveData.value = list
            }
        }
    }


    private fun getCurrencyData(context: Context): CurrencyResponse? {
        val json = parseJson(context, "currencies-json.json")
        val gson = Gson()
        val response = gson.fromJson(json, CurrencyResponse::class.java)
        return response
    }

    private fun getLiveRateData(context: Context): LiveRateResponse? {
        val json = parseJson(context, "live-rates-json.json")
        val gson = Gson()
        val response = gson.fromJson(json, LiveRateResponse::class.java)
        return response
    }

    private fun getWalletBalanceData(context: Context): WalletBalanceResponse? {
        val json = parseJson(context, "wallet-balance-json.json")
        val gson = Gson()
        val response = gson.fromJson(json, WalletBalanceResponse::class.java)
        return response
    }

    /**
     * 解析Json数据
     */
    private fun parseJson(context: Context, jsonName: String): String {
        val newStringBuilder = StringBuilder()
        val inputStream: InputStream?
        try {
            inputStream = context.resources.assets.open(jsonName)
            val isr = InputStreamReader(inputStream)
            val reader = BufferedReader(isr)
            var jsonLine: String?
            while ((reader.readLine().also { jsonLine = it }) != null) {
                newStringBuilder.append(jsonLine)
            }
            reader.close()
            isr.close()
            inputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return newStringBuilder.toString()
    }
}