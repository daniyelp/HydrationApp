package com.daniyelp.hydrationapp.presentation.quantity

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.daniyelp.hydrationapp.BaseViewModel
import com.daniyelp.hydrationapp.data.model.Quantity
import com.daniyelp.hydrationapp.data.model.QuantityUnit
import com.daniyelp.hydrationapp.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

abstract class UpdateQuantityViewModel: BaseViewModel<UpdateQuantityContract.Event, UpdateQuantityContract.State, UpdateQuantityContract.Effect>() {

    abstract fun saveQuantity(quantity: Quantity)

    override fun handleEvents(event: UpdateQuantityContract.Event) {
        when (event) {
            UpdateQuantityContract.Event.Cancel -> cancel()
            UpdateQuantityContract.Event.Save -> save()
            is UpdateQuantityContract.Event.SetQuantity -> setQuantity(event.quantityInput)
        }
    }

    private fun setQuantity(quantityInput: TextFieldValue) {
        setState { copy(quantityInput = quantityInput) }
    }

    private fun cancel() {
        navigateUp()
    }

    private fun save() {
        with(viewState.value) {
            saveQuantity(Quantity(quantityInput.text.toInt(), unit))
        }
        navigateUp()
    }

    private fun navigateUp() {
        setEffect { UpdateQuantityContract.Effect.Navigation.Up }
    }
}