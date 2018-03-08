package com.github.pjozsef.rumorsimulation.algorithm

import com.github.pjozsef.rumorsimulation.datastructure.Graph
import com.github.pjozsef.rumorsimulation.datastructure.GraphNode

class PP1 : RumorAlgorithm() {

    override fun selectValidNodes(nodes: Collection<GraphNode>): Collection<GraphNode> {
        return nodes.shuffled()
    }

    override fun selectNeighbour(graph: Graph<GraphNode>, node: GraphNode): GraphNode? {
        val (selectedNeighbours, unselectedNeighbours) = graph.getNeighbours(node).partition { it.isSelected }
        return if (node.isSelected) {
            unselectedNeighbours.shuffled().firstOrNull()
        } else {
            selectedNeighbours.shuffled().firstOrNull()
        }
    }

    override fun handleNeighbour(currentNode: GraphNode, neighbour: GraphNode, taboo: Set<GraphNode>) {
        if (currentNode.isSelected) {
            neighbour.select()
        } else {
            currentNode.select()
        }
    }
}