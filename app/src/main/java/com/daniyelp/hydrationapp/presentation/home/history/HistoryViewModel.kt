package com.daniyelp.hydrationapp.presentation.home.history

import androidx.lifecycle.viewModelScope
import com.daniyelp.hydrationapp.presentation.BaseViewModel
import com.daniyelp.hydrationapp.data.model.QuantityUnit
import com.daniyelp.hydrationapp.data.repository.DayProgressRepository
import com.daniyelp.hydrationapp.data.repository.impl.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val dayProgressRepository: DayProgressRepository,
    private val preferencesRepository: PreferencesRepository,
): BaseViewModel<HistoryContract.Event, HistoryContract.State, HistoryContract.Effect>() {
    override fun setInitialState(): HistoryContract.State {
        return HistoryContract.State(
            unit = QuantityUnit.Milliliter,
            dayProgressList = emptyList()
        )
    }

    override fun handleEvents(event: HistoryContract.Event) {}

    init {
        dayProgressRepository.all(30).observeForever {
            setState { copy(dayProgressList = it) }
        }
        preferencesRepository.readPreferredUnit(viewModelScope) {
            setState { viewState.value.copy(unit = it) }
        }
    }
}