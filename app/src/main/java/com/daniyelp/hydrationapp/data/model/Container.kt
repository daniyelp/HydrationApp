package com.daniyelp.hydrationapp.data.model

data class Container(val id: Int, val quantity: Quantity) {
    companion object {
        private val containers = listOf(
            Container(1, Quantity(200, QuantityUnit.Milliliter)),
            Container(2, Quantity(400, QuantityUnit.Milliliter)),
            Container(3, Quantity(500, QuantityUnit.Milliliter)),
        )
        fun getContainers() = containers
        fun getContainer(id: Int) = containers.single { it.id == id}
    }
}