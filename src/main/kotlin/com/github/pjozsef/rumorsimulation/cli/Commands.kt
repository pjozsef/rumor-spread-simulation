package com.github.pjozsef.rumorsimulation.cli

import com.beust.jcommander.IStringConverter
import com.beust.jcommander.Parameter
import com.beust.jcommander.Parameters
import com.github.pjozsef.rumorsimulation.algorithm.RumorAlgorithm

sealed class Command {
    companion object {
        const val INFO = "info"
        const val SIMULATION = "simulation"
    }
}

@Parameters(commandDescription = "Print graph information (conductance, vertex expansion)")
class InfoCommand : Command() {
    @Parameter(
            names = ["--graph"],
            required = true,
            description = "Path to graph file")
    lateinit var graphPath: String

    @Parameter(
            names = ["--verbose"],
            description = "Print extra information"
    )
    var verbose: Boolean = false
}

@Parameters(commandDescription = "Run the simulation")
class SimulationCommand : Command() {
    @Parameter(
            names = ["--count"],
            required = true,
            description = "Number of simulations to run")
    var count: Int = -1

    @Parameter(
            names = ["--algorithm"],
            required = true,
            converter = ModeConverter::class,
            description = "The type of the algorithm [Push, Pull, PP0, PP1]")
    lateinit var algorithm: RumorAlgorithm

    @Parameter(
            names = ["--graph"],
            required = true,
            description = "Path to graph file")
    lateinit var graphPath: String

    @Parameter(
            names = ["--csv"],
            required = true,
            description = "Path where the output csv will be saved")
    lateinit var csvPath: String

    @Parameter(
            names = ["--verbose"],
            description = "Print extra information"
    )
    var verbose: Boolean = false
}

@Suppress("UNCHECKED_CAST")
private class ModeConverter : IStringConverter<RumorAlgorithm> {

    companion object {
        val typeMap = mapOf(
                "push" to "Push",
                "pull" to "Pull",
                "pp0" to "PP0",
                "pp1" to "PP1"
        )
    }

    override fun convert(value: String) = try {
        val className: String? = typeMap[value.toLowerCase()]
        requireNotNull(className)
        className?.let {
            val type = Class.forName("com.github.pjozsef.rumorsimulation.algorithm.$it") as Class<RumorAlgorithm>
            type.newInstance()
        } ?: kotlin.run {
            error("Type must be one fo these: [Push, Pull, PP0, PP1]")
        }
    } catch (originalException: Exception){
        throw Exception("Cannot create algorithm type from: $value", originalException)
    }
}