package com.github.pjozsef.rumorsimulation.simulation

data class SimulationRecord(
        val iteration: Int,
        val numberOfSteps: Int,
        val algorithm: String,
        val nodeCount: Int,
        val edgeCount: Int,
        val vertexExpansion: Double,
        val graphConductance: Double)