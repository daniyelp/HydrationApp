package com.daniyelp.hydrationapp.data.model

enum class QuantityUnit {
    Milliliter,
    Ounce;
    companion object {
        fun getUnits() = values().toList()
    }
}