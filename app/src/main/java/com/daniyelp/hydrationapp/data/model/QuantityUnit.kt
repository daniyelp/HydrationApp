package com.daniyelp.hydrationapp.data.model

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase

enum class QuantityUnit {
    Milliliter,
    Ounce;
    companion object {
        fun getUnits() = values().toList()
        fun fromString(str: String): QuantityUnit =
            when(str.toLowerCase(Locale.current)) {
                "milliliters", "ml" -> Milliliter
                "ounces", "oz" -> Ounce
                else -> { throw Exception(/*TODO*/) }
            }
    }
    override fun toString(): String {
        return when(this) {
            Milliliter -> "milliliters"
            Ounce -> "ounces"
        }
    }
    fun toShortString(): String {
        return when(this) {
            Milliliter -> "ml"
            Ounce -> "oz"
        }
    }
}