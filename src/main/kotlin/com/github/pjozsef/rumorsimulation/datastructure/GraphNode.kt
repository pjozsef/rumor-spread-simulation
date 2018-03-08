package com.github.pjozsef.rumorsimulation.datastructure

data class GraphNode(val label: String) {

    var isSelected = false
        private set

    fun select() {
        isSelected = true
    }

}