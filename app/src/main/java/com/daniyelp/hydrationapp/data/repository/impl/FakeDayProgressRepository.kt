package com.daniyelp.hydrationapp.data.repository.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.daniyelp.hydrationapp.data.model.DayProgress
import com.daniyelp.hydrationapp.data.model.Quantity
import com.daniyelp.hydrationapp.data.model.QuantityUnit
import com.daniyelp.hydrationapp.data.repository.DayProgressRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*
import kotlin.random.Random
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi

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
            set(0, first().copy(quantity = Quantity(200, QuantityUnit.Milliliter)))
        }.toList()
    )

    override fun all(last: Int): Flow<List<DayProgress>> =
        Transformations.map(_dayProgressList) { list -> list.take(last) }.asFlow()


    override fun getTodayProgress(): Flow<DayProgress> =
        Transformations.map(_dayProgressList) { list -> list[0] }.asFlow()

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

@ExperimentalCoroutinesApi
fun <T> LiveData<T>.asFlow(): Flow<T> = callbackFlow {
    val observer = Observer<T> { value ->
        trySend(value)
    }
    observeForever(observer)
    awaitClose {
        removeObserver(observer)
    }
}