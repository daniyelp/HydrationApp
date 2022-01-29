package com.daniyelp.hydrationapp.presentation.quantity

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.daniyelp.hydrationapp.data.model.Quantity
import com.daniyelp.hydrationapp.data.model.QuantityUnit
import com.daniyelp.hydrationapp.data.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpdateContainerQuantityViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    savedStateHandle: SavedStateHandle
) : UpdateQuantityViewModel() {

    private val containerId: Int = savedStateHandle.get<Int>("containerId")!!

    init {
        with(preferencesRepository) {
            readPreferredUnit(viewModelScope) { unit ->
                setState {
                    copy(unit = unit)
                }
            }
            readContainerQuantity(containerId, viewModelScope) { quantity ->
                val quantityString = quantity.getValue(viewState.value.unit).toString()
                setState {
                    copy(quantityInput = TextFieldValue(quantityString, TextRange(quantityString.length)))
                }
            }
        }
    }

    override fun setInitialState(): UpdateQuantityContract.State {
        return UpdateQuantityContract.State(
            quantityInput = TextFieldValue(""),
            unit = QuantityUnit.Milliliter
        )
    }

    override fun saveQuantity(quantity: Quantity) {
        preferencesRepository.editContainerQuantity(containerId, viewModelScope, quantity)
    }
}