package com.yyp.cryptodemo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yyp.cryptodemo.adapter.CurrencyListAdapter
import com.yyp.cryptodemo.databinding.ActivityMainBinding
import com.yyp.cryptodemo.viewmodel.DataViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    private val vm: DataViewModel by lazy { DataViewModel() }
    private var adapter: CurrencyListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initView()
        initListener()
        initData()
    }

    private fun initView() {
        adapter = CurrencyListAdapter()
        viewBinding.rvCurrency.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    private fun initListener() {
        vm.walletBalanceLiveData.observe(this) {
            //显示总余额
            viewBinding.tvBalance.text = "$ $it USD"
        }
        vm.currencyListLiveData.observe(this) {
            //显示列表数据
            adapter?.setData(it)
        }
    }

    private fun initData() {
        vm.getAllData(this)
    }
}
