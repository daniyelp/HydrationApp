package com.daniyelp.hydrationapp.data.repository

import com.daniyelp.hydrationapp.data.model.DayProgress
import com.daniyelp.hydrationapp.data.model.Quantity

interface DayProgressRepository {
    suspend fun all(last: Int): List<DayProgress>
    suspend fun getTodayProgress(): DayProgress
    suspend fun updateTodayProgress(newProgress: DayProgress)
    suspend fun updateTodayGoal(newGoal: Quantity)
}