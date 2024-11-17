package com.example.cryptic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class CurrencyObject(
    var data : Map<String,CurrencyData>,
    var meta : CurrencyMeta
)

data class CurrencyData(
    var code : String,
    var value : Float
)

data class CurrencyMeta(
    var last_updated_at : String
)
