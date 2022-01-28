package com.daniyelp.hydrationapp.data.model

//quantity.get(Milliliters); quantity.set(Milliliters)
data class Container(val id: Int, val quantity: Int) {
    companion object {
        private val containers = listOf(
            Container(1, 200),
            Container(2, 400),
            Container(3, 500),
        )
        fun getContainers() = containers
        fun getContainer(id: Int) = containers.single { it.id == id}
    }
}