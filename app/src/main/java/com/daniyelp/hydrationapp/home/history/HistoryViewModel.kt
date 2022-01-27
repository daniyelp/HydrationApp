package com.daniyelp.hydrationapp.home.history

import com.daniyelp.hydrationapp.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

class HistoryViewModel
    : BaseViewModel<HistoryContract.Event, HistoryContract.State, HistoryContract.Effect>() {
    override fun setInitialState(): HistoryContract.State {
        return HistoryContract.State()
    }

    override fun handleEvents(event: HistoryContract.Event) {}
}