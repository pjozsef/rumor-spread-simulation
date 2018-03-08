package com.github.pjozsef.rumorsimulation.algorithm

import com.github.pjozsef.rumorsimulation.datastructure.Graph
import com.github.pjozsef.rumorsimulation.datastructure.GraphNode

class PP0 : RumorAlgorithm() {

    override fun selectValidNodes(nodes: Collection<GraphNode>): Collection<GraphNode> {
        return nodes.shuffled()
    }

    override fun selectNeighbour(graph: Graph<GraphNode>, node: GraphNode): GraphNode? {
        return graph.getNeighbours(node).shuffled().firstOrNull()
    }

    override fun handleNeighbour(currentNode: GraphNode, neighbour: GraphNode, taboo: Set<GraphNode>) {
        if (currentNode.isSelected && !neighbour.isSelected) {
            neighbour.select()
        } else if (neighbour.isSelected) {
            currentNode.select()
        }
    }
}