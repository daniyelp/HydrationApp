package com.daniyelp.hydrationapp.data.model

data class Container(val id: Int, val quantityInMilliliters: Int) {
    companion object {
        private val containers = listOf(
            Container(1, 200),
            Container(2, 200),
            Container(3, 200),
        )
        fun getContainers() = containers
        fun getContainer(id: Int) = containers.single { it.id == id}
    }
}