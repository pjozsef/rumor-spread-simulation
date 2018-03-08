package com.github.pjozsef.rumorsimulation.simulation

fun SimulationRecord.asCsvRow() = buildString {
    append(iteration).append(";")
    append(numberOfSteps).append(";")
    append(algorithm).append(";")
    append(nodeCount).append(";")
    append(edgeCount).append(";")
    append(vertexExpansion).append(";")
    append(graphConductance)
}