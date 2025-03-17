package com.yyp.cryptodemo.bean

/**
 * 列表展示数据 货币余额
 */
data class CurrencyBalanceBean(
    val currencyId: String?, //货币id
    val currencyLogoUrl: String?, //货币图标
    val currencyFullName: String?, //货币全称
    val currencyName: String?, //货币缩写名
    val amount: Double?, //货币个数
    val balance: Double?, //账户余额 单位美元
)
