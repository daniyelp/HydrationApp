package com.daniyelp.hydrationapp.data.repository

import com.daniyelp.hydrationapp.data.model.DayProgress
import com.daniyelp.hydrationapp.data.model.Quantity
import kotlinx.coroutines.flow.Flow

interface DayProgressRepository {
    fun all(last: Int): Flow<List<DayProgress>>
    fun getTodayProgress(): Flow<DayProgress>
    suspend fun updateTodayProgress(newProgress: DayProgress)
    suspend fun updateTodayGoal(newGoal: Quantity)
}