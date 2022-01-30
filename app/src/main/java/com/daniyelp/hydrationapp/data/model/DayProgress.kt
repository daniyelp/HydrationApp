package com.daniyelp.hydrationapp.data.model

data class DayProgress(
    val date: Long,
    val quantity: Quantity,
    val goal: Quantity,
    val id: Int
)