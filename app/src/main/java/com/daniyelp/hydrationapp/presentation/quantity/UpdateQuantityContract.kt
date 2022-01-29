package com.daniyelp.hydrationapp.presentation.quantity

import androidx.compose.ui.text.input.TextFieldValue
import com.daniyelp.hydrationapp.ViewEvent
import com.daniyelp.hydrationapp.ViewSideEffect
import com.daniyelp.hydrationapp.ViewState
import com.daniyelp.hydrationapp.data.model.QuantityUnit

class UpdateQuantityContract {
    sealed class Event : ViewEvent {
        data class SetQuantity(val quantityInput: TextFieldValue): Event()
        object Cancel: Event()
        object Save: Event()
    }

    data class State(
        val quantityInput: TextFieldValue = TextFieldValue(),
        val unit: QuantityUnit
    ) : ViewState

    sealed class Effect : ViewSideEffect {
        sealed class Navigation: Effect() {
            object Up: Navigation()
        }
    }
}