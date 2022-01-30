package com.daniyelp.hydrationapp.data.repository.impl

import com.daniyelp.hydrationapp.data.model.DayProgress
import com.daniyelp.hydrationapp.data.model.Quantity
import com.daniyelp.hydrationapp.data.model.QuantityUnit
import com.daniyelp.hydrationapp.data.repository.DayProgressRepository
import java.util.*
import kotlin.random.Random

private const val millisecondsInADay = 86_400_000

val dayProgressList = (0..40).map {
    DayProgress(
        date = Date().time - millisecondsInADay * it,
        quantity = Quantity(Random.nextInt(1000, 2500) / 100 * 100, QuantityUnit.Milliliter),
        goal = Quantity(2000, QuantityUnit.Milliliter),
        id = it
    )
}
class FakeDayProgressRepository: DayProgressRepository {
    override suspend fun all(): List<DayProgress> {
        return dayProgressList
    }
}