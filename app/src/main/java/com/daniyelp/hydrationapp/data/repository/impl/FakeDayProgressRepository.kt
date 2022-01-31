package com.daniyelp.hydrationapp.data.repository.impl

import com.daniyelp.hydrationapp.data.model.DayProgress
import com.daniyelp.hydrationapp.data.model.Quantity
import com.daniyelp.hydrationapp.data.model.QuantityUnit
import com.daniyelp.hydrationapp.data.repository.DayProgressRepository
import java.util.*
import kotlin.random.Random

private const val millisecondsInADay = 86_400_000

fun generateFakeDayProgressList(n: Int) =
    (0 until n).map {
        DayProgress(
            date = Date().time - millisecondsInADay * it,
            quantity = Quantity(Random.nextInt(1000, 2500) / 100 * 100, QuantityUnit.Milliliter),
            goal = Quantity(2000, QuantityUnit.Milliliter),
            id = it
        )
    }

class FakeDayProgressRepository: DayProgressRepository {
    private val dayProgressList = generateFakeDayProgressList(40).toMutableList().apply {
        add(0, first().copy(quantity = Quantity(0, QuantityUnit.Milliliter)))
    }

    override suspend fun all(n: Int): List<DayProgress> {
        return dayProgressList.take(n)
    }

    override suspend fun getTodayProgress(): DayProgress{
        println("quantity is ${dayProgressList[0].goal.getValue(QuantityUnit.Milliliter)}")
        return dayProgressList[0]
    }

    override suspend fun updateTodayProgress(newProgress: DayProgress) {
        dayProgressList[0] = newProgress
    }

    override suspend fun updateTodayGoal(newGoal: Quantity) {
        dayProgressList[0] = dayProgressList[0].copy(goal = newGoal)
        println("new goal is ${newGoal.getValue(QuantityUnit.Milliliter)}")
    }
}