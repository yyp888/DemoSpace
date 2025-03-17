package com.yyp.cryptodemo.bean

data class WalletBalanceBean(
    val currency: String?,
    val amount: Double?,
)


data class WalletBalanceResponse(
    val ok: Boolean?,
    val warning: String?,
    val wallet: List<WalletBalanceBean>?,
)