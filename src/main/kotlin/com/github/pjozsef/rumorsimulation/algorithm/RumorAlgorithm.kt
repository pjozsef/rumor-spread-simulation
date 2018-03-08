package com.github.pjozsef.rumorsimulation.algorithm

import com.github.pjozsef.rumorsimulation.datastructure.Graph
import com.github.pjozsef.rumorsimulation.datastructure.GraphNode
import java.util.*

abstract class RumorAlgorithm {

    abstract fun selectValidNodes(nodes: Collection<GraphNode>): Collection<GraphNode>

    abstract fun selectNeighbour(graph: Graph<GraphNode>, node: GraphNode): GraphNode?

    abstract fun handleNeighbour(currentNode: GraphNode, neighbour: GraphNode, taboo: Set<GraphNode>)

    fun run(graph: Graph<GraphNode>): Int {
        var rumourSpread = false
        var i = 0
        while (!rumourSpread) {
            val taboo = HashSet<GraphNode>(graph.nodes.size)
            selectValidNodes(graph.nodes).forEach {
                if (!taboo.contains(it)) {
                    taboo += it
                    val neighbour = selectNeighbour(graph, it)
                    if (neighbour != null && !taboo.contains(neighbour)) {
                        taboo += neighbour
                        handleNeighbour(it, neighbour, taboo)
                    }
                }
            }
            rumourSpread = graph.nodes.all { it.isSelected }
            ++i
        }
        return i
    }

}