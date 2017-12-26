package com.github.rubensousa.adventofcode2017

import java.io.BufferedReader
import java.io.FileReader
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet
import kotlin.collections.LinkedHashSet


object Day24 {


    data class Component(val firstPort: Int, val secondPort: Int)

    data class Result(val length: Int, val strength: Int)

    class ComponentNode(val component: Component) {

        var parents = LinkedHashSet<Component>()
        var firstPortConnected = false
        var secondPortConnected = false

        fun connect(node: ComponentNode): Boolean {
            if (node.component == component || parents.contains(node.component)) {
                return false
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

        override fun hashCode(): Int {
            return component.hashCode()
        }

        override fun toString(): String {
            return component.toString()
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ComponentNode

            if (component != other.component) return false
            if (parents != other.parents) return false
            if (firstPortConnected != other.firstPortConnected) return false
            if (secondPortConnected != other.secondPortConnected) return false

            return true
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
        val roots = HashSet<ComponentNode>()
        val nodes = ArrayList<ComponentNode>()

        for (component in components) {
            if (component.firstPort == 0) {
                roots.add(ComponentNode(component))
            }
            nodes.add(ComponentNode(component))
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
        val roots = HashSet<ComponentNode>()
        val nodes = ArrayList<ComponentNode>()

        for (component in components) {
            if (component.firstPort == 0) {
                roots.add(ComponentNode(component))
            }
            nodes.add(ComponentNode(component))
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

    private fun getMaxLengthAndStrength(root: ComponentNode, nodes: List<ComponentNode>): Result {
        var strength = root.component.firstPort + root.component.secondPort
        var length = 1

        val stack = Stack<ComponentNode>()
        stack.add(root)

        while (!stack.isEmpty()) {
            val currentNode = stack.pop()
            val stackSize = stack.size
            for (node in nodes) {
                val nodeCopy = ComponentNode(node.component)
                if (currentNode.connect(nodeCopy)) {
                    stack.add(nodeCopy)
                    nodeCopy.parents = LinkedHashSet(currentNode.parents)
                    nodeCopy.parents.add(currentNode.component)
                }
            }
            if (stackSize == stack.size && currentNode.parents.size + 1 >= length) {
                length = currentNode.parents.size + 1
                strength = Math.max(strength, countStrength(currentNode))
            }
        }
        return Result(length, strength)
    }

    private fun getMaxStrength(root: ComponentNode, nodes: List<ComponentNode>): Int {
        var strength = 0
        val stack = Stack<ComponentNode>()
        stack.add(root)

        while (!stack.isEmpty()) {
            val currentNode = stack.pop()
            val stackSize = stack.size
            for (node in nodes) {
                val nodeCopy = ComponentNode(node.component)
                if (currentNode.connect(nodeCopy)) {
                    stack.add(nodeCopy)
                    nodeCopy.parents = LinkedHashSet(currentNode.parents)
                    nodeCopy.parents.add(currentNode.component)
                }
            }
            if (stack.size == stackSize) {
                // No elements added, so we can count the strength
                // from this node to the top
                strength = Math.max(strength, countStrength(currentNode))
            }
        }

        return strength
    }

    private fun countStrength(node: ComponentNode): Int {
        var strength = node.component.firstPort + node.component.secondPort

        for (parent in node.parents) {
            strength += parent.firstPort + parent.secondPort
        }

        return strength
    }

}