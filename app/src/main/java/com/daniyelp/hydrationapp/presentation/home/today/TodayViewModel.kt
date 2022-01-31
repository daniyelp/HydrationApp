package com.daniyelp.hydrationapp.presentation.home.today

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.daniyelp.hydrationapp.data.model.*
import com.daniyelp.hydrationapp.presentation.BaseViewModel
import com.daniyelp.hydrationapp.data.repository.DayProgressRepository
import com.daniyelp.hydrationapp.data.repository.impl.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
            is TodayContract.Event.SelectContainer -> selectContainer(event.containerId)
        }
    }

    private var todayProgress: DayProgress? = null
    init {
        preferencesRepository.readPreferredUnit(viewModelScope) {
            setState { viewState.value.copy(unit = it) }
        }
        preferencesRepository.readContainers(viewModelScope) {
            setState { viewState.value.copy(containers = it)}
        }
        viewModelScope.launch {
            todayProgress = dayProgressRepository.getTodayProgress()
            setState {
                copy(
                    dailyGoal = todayProgress!!.goal,
                    currentQuantity = todayProgress!!.quantity
                )
            }
        }
    }

    private fun selectContainer(containerId: Int) {
        val quantityAdded = viewState.value.containers.first { it.id == containerId }.quantity
        todayProgress = with(todayProgress!!) { copy(quantity = quantity + quantityAdded) }
        setState { viewState.value.copy(currentQuantity = todayProgress!!.quantity) }
        viewModelScope.launch {
            dayProgressRepository.updateTodayProgress(todayProgress!!)
        }
    }

    private fun navigateToSettings() {
        setEffect { TodayContract.Effect.Navigation.ToSettings }
    }
}