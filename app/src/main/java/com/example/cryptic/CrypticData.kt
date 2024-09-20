package com.example.cryptic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CrypticData(
    var name: String,
    var symbol: String,
    var current_price: Float,
    var price_change_percentage_24h: Float
)