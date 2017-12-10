package com.github.rubensousa.adventofcode2017


import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

object Day7 {

    class Node {
        var head: Boolean = false
        var weight: Int = 0
        var word: String = ""
        var childNodes: ArrayList<Node> = ArrayList()

        override fun toString(): String {
            return word
        }
    }

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val bufferedReader = BufferedReader(FileReader("inputday7.txt"))
        var line: String? = bufferedReader.readLine()
        val nodes = ArrayList<Node>()
        val nodeMap = LinkedHashMap<String, Node>()

        while (line != null) {
            val node = parseNode(line, nodeMap)
            if (!node.childNodes.isEmpty()) {
                nodes.add(node)
            }
            line = bufferedReader.readLine()
        }

        placeProperChilds(nodes, nodeMap)

        val head = getBottomProgram(nodes)
        head!!.head = true
        println(head)
        println(getUnbalancedNodeWeight(head))
    }

    private fun placeProperChilds(nodes: ArrayList<Node>, map: Map<String, Node>) {
        for (i in nodes.indices) {
            val node = nodes[i]
            if (map.containsKey(node.word)) {
                nodes[i] = map[node.word]!!
            }
            placeProperChilds(node.childNodes, map)
        }
    }

    private fun getUnbalancedNodeWeight(head: Node): Int {

        var correctWeight = 0
        var incorrectWeight = 0
        val weights = HashSet<Int>()

        for (child in head.childNodes) {
            val weight = getWeight(child)
            if (!weights.contains(weight)) {
                weights.add(weight)
            } else {
                correctWeight = weight
            }
        }

        for (integer in weights) {
            if (integer != correctWeight) {
                incorrectWeight = integer
            }
        }

        for (child in head.childNodes) {
            val childWeight = getWeight(child)
            if (childWeight == incorrectWeight) {
                val weight = getUnbalancedNodeWeight(child)
                // If all childs are balanced,
                // return the weight this child should have
                if (weight == 0) {
                    if (head.head) {
                        return correctWeight
                    } else {
                        return child.weight + correctWeight - incorrectWeight
                    }
                } else {
                    return weight
                }
            }
        }

        // It's 0 if all childs are balanced
        return incorrectWeight
    }

    private fun getWeight(node: Node): Int {
        var weight = node.weight
        for (n in node.childNodes) {
            weight += getWeight(n)
        }
        return weight
    }

    private fun parseNode(text: String, map: MutableMap<String, Node>): Node {
        val data = text.split(" ".toRegex())
        val node = Node()
        node.word = data[0]
        node.weight = Integer.parseInt(data[1].replace("\\(".toRegex(), "").replace("\\)".toRegex(), ""))
        map.put(node.word, node)
        for (i in 3 until data.size) {
            val word = data[i].replace(",".toRegex(), "")
            var child: Node? = map[word]
            if (child == null) {
                child = Node()
                child.word = word
            }
            node.childNodes.add(child)
        }
        return node
    }

    /**
     * The bottom program will be the only program
     * that won't appear inside other program's childs
     */
    private fun getBottomProgram(nodes: List<Node>): Node? {
        for (i in 0 until nodes.size - 1) {
            val node = nodes[i]
            var found = false
            searchNode@
            for (j in nodes.indices) {
                val childNodes = nodes[j].childNodes
                for (n in childNodes) {
                    if (n.word == node.word) {
                        found = true
                        break@searchNode
                    }
                }
            }
            if (!found) {
                return node
            }
        }
        return null
    }

}