package com.daniyelp.hydrationapp.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

interface ViewState

interface ViewEvent

interface ViewSideEffect

abstract class BaseViewModel<Event : ViewEvent, UiState : ViewState, Effect : ViewSideEffect> :
    ViewModel() {

    private val initialState: UiState by lazy { setInitialState() }
    protected abstract fun setInitialState(): UiState

    private val _viewState: MutableState<UiState> = mutableStateOf(initialState)
    val viewState: State<UiState> = _viewState

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()

    private val _effect: MutableSharedFlow<Effect> = MutableSharedFlow()
    val effect: SharedFlow<Effect> = _effect

    protected abstract fun handleEvents(event: Event)

    init {
        _event.onEach { event ->
            handleEvents(event)
        }.launchIn(viewModelScope)
    }

    protected fun setState(reducer: UiState.() -> UiState) {
        _viewState.value = _viewState.value.reducer()
    }

    fun sendEvent(event: Event) {
        viewModelScope.launch { _event.emit(event) }
    }

    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch { _effect.emit(effect) }
    }
}
