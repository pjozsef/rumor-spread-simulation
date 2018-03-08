package com.github.pjozsef.rumorsimulation

import com.beust.jcommander.JCommander
import com.beust.jcommander.ParameterException
import com.github.pjozsef.rumorsimulation.cli.Command
import com.github.pjozsef.rumorsimulation.cli.InfoCommand
import com.github.pjozsef.rumorsimulation.cli.SimulationCommand
import com.github.pjozsef.rumorsimulation.datastructure.Graph
import com.github.pjozsef.rumorsimulation.datastructure.GraphNode
import com.github.pjozsef.rumorsimulation.datastructure.HashGraph
import com.github.pjozsef.rumorsimulation.simulation.SimulationRecord
import com.github.pjozsef.rumorsimulation.simulation.asCsvRow
import com.github.pjozsef.rumorsimulation.util.conductance
import com.github.pjozsef.rumorsimulation.util.vertexExpansion
import java.io.File
import java.io.FileWriter

fun main(args: Array<String>) {
    val infoCommand = InfoCommand()
    val simulationCommand = SimulationCommand()
    val jcommander = JCommander.newBuilder()
            .addCommand(Command.INFO, infoCommand)
            .addCommand(Command.SIMULATION, simulationCommand)
            .build()

    try {
        jcommander.parse(*args)
        when (jcommander.parsedCommand) {
            Command.INFO -> printGraphInfo(infoCommand)
            Command.SIMULATION -> runSimulation(simulationCommand)
            else -> jcommander.usage()
        }
    } catch (pe: ParameterException) {
        println(pe.message)
        pe.usage()
    }
    System.exit(0)
}

fun printGraphInfo(infoCommand: InfoCommand) {
    val graph = readGraph(infoCommand.graphPath, infoCommand.verbose, verify = true)
    println("Graph conductance: ${graph.conductance()}")
    println("Vergex expansion: ${graph.vertexExpansion()}")
}

fun runSimulation(simulationCommand: SimulationCommand) {
    val algorithm = simulationCommand.algorithm::class.java.simpleName
    val inputGraph = readGraph(simulationCommand.graphPath, simulationCommand.verbose, verify = true)
    val nodeCount = inputGraph.nodes.size
    val edgeCount = inputGraph.edges.size
    val vertexExpansion = inputGraph.vertexExpansion()
    val graphConductance = inputGraph.conductance()

    val records = (0 until simulationCommand.count).map {
        println("Iteration $it")
        val graph = readGraph(simulationCommand.graphPath)
        val steps = simulationCommand.algorithm.run(graph)
        it to steps
    }.map { (iteration, step) ->
        SimulationRecord(iteration, step, algorithm, nodeCount, edgeCount, vertexExpansion, graphConductance)
    }

    FileWriter(simulationCommand.csvPath).use { writer ->
        val header = "iteration;numberOfSteps;algorithm;nodeCount;edgeCount;vertexExpansion;graphConductance"
        writer.appendln(header)
        records.forEach {
            writer.appendln(it.asCsvRow())
        }
    }
}

fun readGraph(path: String, verbose: Boolean = false, verify: Boolean = false): Graph<GraphNode> {
    val (selected, rawNodes, rawEdges) = File(path).readText().split("###").map { it.replace(Regex("\\s"), "") }
    if (verbose) {
        println("Selected node: $selected")
        println("Raw nodes: $rawNodes")
        println("Raw edges: $rawEdges")
    }
    val graph = HashGraph<GraphNode>()
    val nodes = rawNodes.split(",")
            .map {
                val node = GraphNode(it)
                if (it == selected) {
                    node.select()
                }
                node
            }
    nodes.forEach {
        graph.addNode(it)
    }
    val nodeMap = nodes.associate {  it.label to it }
    rawEdges.split(",")
            .map {
                val (from, to) = it.split("-")
                from to to
            }.forEach { (fromLabel, toLabel) ->
                val from = nodeMap[fromLabel] ?: error("Undefined node $fromLabel in edge $fromLabel-$toLabel")
                val to = nodeMap[toLabel] ?: error("Undefined node $fromLabel in edge $fromLabel-$toLabel")
                graph.addEdge(from, to)
            }
    if (verify) {
        require(graph.nodes.size == HashSet(graph.nodes).size, {"Duplicate nodes found!"})
        require(graph.isConnected(), { "Graph must be connected!" })
    }
    return graph
}