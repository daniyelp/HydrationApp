package com.daniyelp.hydrationapp.presentation.home.today

import androidx.lifecycle.viewModelScope
import com.daniyelp.hydrationapp.data.model.*
import com.daniyelp.hydrationapp.presentation.BaseViewModel
import com.daniyelp.hydrationapp.data.repository.DayProgressRepository
import com.daniyelp.hydrationapp.data.repository.impl.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodayViewModel @Inject constructor(
    private val dayProgressRepository: DayProgressRepository,
    private val preferencesRepository: PreferencesRepository,
): BaseViewModel<TodayContract.Event, TodayContract.State, TodayContract.Effect>() {
    override fun setInitialState(): TodayContract.State {
        return TodayContract.State(
            QuantityUnit.Milliliter,
            Quantity(0, QuantityUnit.Milliliter),
            Quantity(0, QuantityUnit.Milliliter),
            emptyList()
        )
    }

    override fun handleEvents(event: TodayContract.Event) {
        when(event) {
            TodayContract.Event.NavigateToSettings -> navigateToSettings()
            TodayContract.Event.Undo -> undo()
            is TodayContract.Event.SelectContainer -> selectContainer(event.containerId)
        }
    }
    private val quantitiesAddedDeque = ArrayDeque<Quantity>()
    private val todayProgressFlow: Flow<DayProgress> = dayProgressRepository.getTodayProgress()
    private var todayProgress: DayProgress? = null
    init {
        preferencesRepository.readPreferredUnit(viewModelScope) {
            setState { copy(unit = it) }
        }
        preferencesRepository.readContainers(viewModelScope) {
            setState { copy(containers = it)}
        }
        todayProgressFlow.onEach { todayProgress ->
            this.todayProgress = todayProgress
            setState {
                copy(
                    dailyGoal = todayProgress.goal,
                    currentQuantity = todayProgress.quantity
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun selectContainer(containerId: Int) {
        todayProgress?.let { todayProgress ->
            val quantityAdded = viewState.value.containers.first { it.id == containerId }.quantity
            val newTodayProgress = with(todayProgress) { copy(quantity = quantity + quantityAdded) }
            viewModelScope.launch {
                dayProgressRepository.updateTodayProgress(newTodayProgress)
            }
            quantitiesAddedDeque.addLast(quantityAdded)
            setState { copy(undosAvailable = undosAvailable + 1) }
        }
    }

    private fun undo() {
        todayProgress?.let { todayProgress ->
            val quantityToRemove = quantitiesAddedDeque.removeLast()
            val newTodayProgress = with(todayProgress) { copy(quantity = quantity - quantityToRemove) }
            viewModelScope.launch {
                dayProgressRepository.updateTodayProgress(newTodayProgress)
            }
            setState { copy(undosAvailable = undosAvailable - 1) }
        }
    }

    private fun navigateToSettings() {
        sendEffect(TodayContract.Effect.Navigation.ToSettings)
    }
}