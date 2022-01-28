package com.daniyelp.hydrationapp.quantity

import androidx.compose.ui.text.input.TextFieldValue
import com.daniyelp.hydrationapp.BaseViewModel
import com.daniyelp.hydrationapp.data.model.QuantityUnit

class QuantityViewModel
    : BaseViewModel<QuantityContract.Event, QuantityContract.State, QuantityContract.Effect>() {
    override fun setInitialState(): QuantityContract.State {
        return QuantityContract.State(
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