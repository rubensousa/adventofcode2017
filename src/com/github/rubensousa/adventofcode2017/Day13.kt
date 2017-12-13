package com.github.rubensousa.adventofcode2017

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

object Day13 {

    class InputData {
        var lastLayer = 0
        var layers: Map<Int, Scanner> = HashMap()
    }

    class Scanner {
        var layer = 0
        var index = 0
        var size = 0
        private var increment = true

        fun incrementScan() {
            if (increment) {
                index++
                if (index >= size) {
                    index = size - 2
                    increment = false
                }
            } else {
                index--
                if (index < 0) {
                    index = 1
                    increment = true
                }
            }
        }

        override fun toString(): String {
            return layer.toString() + ":" + size.toString() + ":" + index.toString()
        }
    }

    private fun parseLayers(): InputData {
        val bufferedReader = BufferedReader(FileReader("inputday13.txt"))
        var line: String? = bufferedReader.readLine()
        val data = InputData()
        val map = HashMap<Int, Scanner>()

        while (line != null) {
            val number = line.substring(0, line.indexOf(":"))
            val size = line.substring(line.indexOf(" ") + 1, line.length)
            val scanner = Scanner()
            scanner.index = 0
            scanner.size = size.toInt()
            scanner.layer = number.toInt()
            map.put(number.toInt(), scanner)
            data.lastLayer = scanner.layer
            line = bufferedReader.readLine()
        }
        data.layers = map
        return data
    }

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val data = parseLayers()
        println(getSeverity(data.layers, data.lastLayer))
        println(getMinDelay(data.layers))
    }

    private fun getSeverity(layers: Map<Int, Scanner>, lastLayer: Int): Int {
        var severity = 0
        for (layer in 0 until lastLayer + 1) {
            val scanner = layers[layer]
            if (scanner != null && scanner.index == 0) {
                severity += scanner.layer.times(scanner.size)
            }
            moveScanners(layers)
        }
        return severity
    }

    private fun getMinDelay(layers: Map<Int, Scanner>): Int {
        var delay = 1
        var caught = true
        while (caught) {
            for (layer in layers.keys) {
                val scanner = layers[layer]!!
                val jumps = (scanner.size - 1) * 2
                caught = (scanner.layer + delay).rem(jumps) == 0
                if (caught) {
                    delay++
                    break
                }
            }
        }
        return delay
    }

    private fun moveScanners(layers: Map<Int, Scanner>) {
        for (layer in layers.keys) {
            layers[layer]!!.incrementScan()
        }
    }
}