package com.yyp.cryptodemo.bean

data class LiveRateBean(
    val from_currency: String?,
    val to_currency: String?,
    val rates: List<LiveReteItem>?,
    val time_stamp: Long?,
)

data class LiveReteItem(
    val amount: Double?,
    val rate: Double?
)

data class LiveRateResponse(
    val ok: Boolean?,
    val warning: String?,
    val tiers: List<LiveRateBean>?,
)
