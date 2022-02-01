package com.daniyelp.hydrationapp.data.repository

import androidx.lifecycle.LiveData
import com.daniyelp.hydrationapp.data.model.DayProgress
import com.daniyelp.hydrationapp.data.model.Quantity
import kotlinx.coroutines.flow.Flow

interface DayProgressRepository {
    fun all(last: Int): LiveData<List<DayProgress>>
    fun getTodayProgress(): LiveData<DayProgress>
    suspend fun updateTodayProgress(newProgress: DayProgress)
    suspend fun updateTodayGoal(newGoal: Quantity)
}