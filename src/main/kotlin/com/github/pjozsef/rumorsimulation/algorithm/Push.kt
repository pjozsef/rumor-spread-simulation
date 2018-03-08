package com.github.pjozsef.rumorsimulation.algorithm

import com.github.pjozsef.rumorsimulation.datastructure.Graph
import com.github.pjozsef.rumorsimulation.datastructure.GraphNode

class Push : RumorAlgorithm() {

    override fun selectValidNodes(nodes: Collection<GraphNode>): Collection<GraphNode> {
        return nodes.shuffled().filter { it.isSelected }
    }

    override fun selectNeighbour(graph: Graph<GraphNode>, node: GraphNode): GraphNode? {
        return graph.getNeighbours(node).shuffled().firstOrNull()
    }

    override fun handleNeighbour(currentNode: GraphNode, neighbour: GraphNode, taboo: Set<GraphNode>) {
        if (!neighbour.isSelected) {
            neighbour.select()
        }
    }

}