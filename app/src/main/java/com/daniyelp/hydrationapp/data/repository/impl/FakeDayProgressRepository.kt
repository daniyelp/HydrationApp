package com.daniyelp.hydrationapp.data.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.daniyelp.hydrationapp.data.model.DayProgress
import com.daniyelp.hydrationapp.data.model.Quantity
import com.daniyelp.hydrationapp.data.model.QuantityUnit
import com.daniyelp.hydrationapp.data.repository.DayProgressRepository
import java.util.*
import kotlin.random.Random

private const val millisecondsInADay: Long = 86_400_000

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
    private val _dayProgressList = MutableLiveData(
        generateFakeDayProgressList(40).toMutableList().apply {
            add(0, first().copy(quantity = Quantity(0, QuantityUnit.Milliliter)))
        }.toList()
    )

    override fun all(n: Int): LiveData<List<DayProgress>> =
        Transformations.map(_dayProgressList) { list -> list.take(n) }


    override fun getTodayProgress(): LiveData<DayProgress> =
        Transformations.map(_dayProgressList) { list -> list[0] }

    override suspend fun updateTodayProgress(newProgress: DayProgress) {
        _dayProgressList.postValue(_dayProgressList.value?.toMutableList()?.apply { set(0, newProgress) }?.toList())
    }

    override suspend fun updateTodayGoal(newGoal: Quantity) {
        _dayProgressList.value?.let { dayProgressList ->
            val newTodayProgress = dayProgressList[0].copy(goal = newGoal)
            updateTodayProgress(newTodayProgress)
        }
    }
}