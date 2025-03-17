package com.yyp.cryptodemo.bean

data class CurrencyBean(
    val coin_id: String?,
    val name: String?,
    val symbol: String?,
    val colorful_image_url: String?,
)

data class CurrencyResponse(
    val currencies: List<CurrencyBean>?
)
