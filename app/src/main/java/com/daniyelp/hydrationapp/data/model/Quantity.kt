package com.daniyelp.hydrationapp.data.model

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase

class Quantity(value: Int, unit: QuantityUnit) {
    private val value: Int = value * unit.toAnotherRatio(QuantityUnit.Milliliter).toInt()
    fun getValue(unit: QuantityUnit): Int =
        value * QuantityUnit.Milliliter.toAnotherRatio(unit).toInt()
}

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
    fun toAnotherRatio(another: QuantityUnit): Float {
        return when(this) {
            Milliliter -> when(another) {
                Milliliter -> 1f
                Ounce -> 1 / 29.574f
            }
            Ounce -> when(another) {
                Milliliter -> 29.574f
                Ounce -> 1f
            }
        }
    }
}