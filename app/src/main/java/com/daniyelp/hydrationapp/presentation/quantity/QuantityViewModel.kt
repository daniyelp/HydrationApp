package com.daniyelp.hydrationapp.presentation.quantity

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.daniyelp.hydrationapp.BaseViewModel
import com.daniyelp.hydrationapp.data.model.QuantityUnit
import com.daniyelp.hydrationapp.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuantityViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
): BaseViewModel<QuantityContract.Event, QuantityContract.State, QuantityContract.Effect>() {
    override fun setInitialState(): QuantityContract.State {
        return QuantityContract.State(
            quantityInput = TextFieldValue(""),
            unit = QuantityUnit.Milliliter
        )
    }

    override fun handleEvents(event: QuantityContract.Event) {
        when (event) {
            QuantityContract.Event.Cancel -> cancel()
            QuantityContract.Event.Save -> save()
            is QuantityContract.Event.SetQuantity -> setQuantity(event.quantityInput)
        }
    }

    init {
        preferencesRepository.readPreferredUnit(viewModelScope) {
            setState { viewState.value.copy(unit = it) }
        }
    }

    private fun setQuantity(quantityInput: TextFieldValue) {
        setState { viewState.value.copy(quantityInput = quantityInput) }
    }

    private fun cancel() {
        navigateUp()
    }

    private fun save() {
        navigateUp()
    }

    private fun navigateUp() {
        setEffect { QuantityContract.Effect.Navigation.Up }
    }
}