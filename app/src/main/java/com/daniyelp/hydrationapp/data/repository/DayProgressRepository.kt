package com.daniyelp.hydrationapp.data.repository

import com.daniyelp.hydrationapp.data.model.DayProgress

interface DayProgressRepository {
    suspend fun all(): List<DayProgress>
}