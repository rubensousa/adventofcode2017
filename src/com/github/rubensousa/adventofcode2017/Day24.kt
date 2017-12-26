package com.github.rubensousa.adventofcode2017

import java.io.BufferedReader
import java.io.FileReader
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


object Day24 {


    data class Component(val firstPort: Int, val secondPort: Int)

    data class Result(val length: Int, val strength: Int)

    class Node(val component: Component) {

        var parent: Node? = null
        var length = 1
        var strength = component.firstPort + component.secondPort
        var firstPortConnected = false
        var secondPortConnected = false

        override fun toString(): String {
            return component.toString()
        }

        fun connect(node: Node): Boolean {
            if (node.component == component) {
                return false
            }
            var currentParent = parent
            while (currentParent != null) {
                if (currentParent.component == node.component) {
                    return false
                }
                currentParent = currentParent.parent
            }
            if (!firstPortConnected) {
                if (component.firstPort == node.component.firstPort && !node.firstPortConnected) {
                    node.firstPortConnected = true
                    return true
                } else if (component.firstPort == node.component.secondPort && !node.secondPortConnected) {
                    node.secondPortConnected = true
                    return true
                }
            }
            if (!secondPortConnected) {
                if (component.secondPort == node.component.firstPort && !node.firstPortConnected) {
                    node.firstPortConnected = true
                    return true
                } else if (component.secondPort == node.component.secondPort && !node.secondPortConnected) {
                    node.secondPortConnected = true
                    return true
                }
            }
            return false
        }
    }

    fun parseComponents(): ArrayList<Component> {
        val components = ArrayList<Component>()
        val reader = BufferedReader(FileReader("inputday24.txt"))
        var line = reader.readLine()
        while (line != null) {
            val data = line.split("/")
            val firstPort = data[0].toInt()
            val secondPort = data[1].toInt()
            if (firstPort > secondPort) {
                components.add(Component(secondPort, firstPort))
            } else {
                components.add(Component(firstPort, secondPort))
            }
            line = reader.readLine()
        }
        return components
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val components = parseComponents()
        println(getStrongestBridge(components))
        println(getLongestStrongestBridge(components))
    }

    private fun getStrongestBridge(components: List<Component>): Int {
        var strength = 0
        val roots = HashSet<Node>()
        val nodes = ArrayList<Node>()

        for (component in components) {
            if (component.firstPort == 0) {
                roots.add(Node(component))
            }
            nodes.add(Node(component))
        }

        for (root in roots) {
            root.firstPortConnected = true
            strength = Math.max(strength, getMaxStrength(root, nodes))
        }

        return strength
    }

    private fun getLongestStrongestBridge(components: List<Component>): Int {
        var strength = 0
        var length = 0
        val roots = HashSet<Node>()
        val nodes = ArrayList<Node>()

        for (component in components) {
            if (component.firstPort == 0) {
                roots.add(Node(component))
            }
            nodes.add(Node(component))
        }

        for (root in roots) {
            root.firstPortConnected = true
            val result = getMaxLengthAndStrength(root, nodes)
            if (result.length >= length) {
                strength = Math.max(result.strength, strength)
                length = result.length
            }
        }

        return strength
    }

    private fun getMaxLengthAndStrength(root: Node, nodes: List<Node>): Result {
        var strength = root.component.firstPort + root.component.secondPort
        var length = 1

        val stack = Stack<Node>()
        stack.add(root)

        while (!stack.isEmpty()) {
            val currentNode = stack.pop()
            val stackSize = stack.size
            for (node in nodes) {
                val nodeCopy = Node(node.component)
                if (currentNode.connect(nodeCopy)) {
                    nodeCopy.strength += currentNode.strength
                    nodeCopy.length = currentNode.length + 1
                    nodeCopy.parent = currentNode
                    stack.add(nodeCopy)
                }
            }
            if (stackSize == stack.size && currentNode.length >= length) {
                length = currentNode.length
                strength = Math.max(strength, currentNode.strength)
            }
        }
        return Result(length, strength)
    }

    private fun getMaxStrength(root: Node, nodes: List<Node>): Int {
        var strength = 0
        val stack = Stack<Node>()
        stack.add(root)

        while (!stack.isEmpty()) {
            val currentNode = stack.pop()
            val stackSize = stack.size
            for (node in nodes) {
                val nodeCopy = Node(node.component)
                if (currentNode.connect(nodeCopy)) {
                    nodeCopy.strength += currentNode.strength
                    nodeCopy.length = currentNode.length + 1
                    nodeCopy.parent = currentNode
                    stack.add(nodeCopy)
                }
            }
            if (stack.size == stackSize) {
                // No elements added, so we can count the strength
                // from this node to the top
                strength = Math.max(strength, currentNode.strength)
            }
        }

        return strength
    }
}