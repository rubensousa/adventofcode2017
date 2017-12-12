package com.github.rubensousa.adventofcode2017

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import kotlin.collections.ArrayList

object Day12 {

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val bufferedReader = BufferedReader(FileReader("inputday12.txt"))
        var line: String? = bufferedReader.readLine()
        val programs = ArrayList<ArrayList<Int>>()
        while (line != null) {
            val values = parsePrograms(line)
            programs.add(values)
            line = bufferedReader.readLine()
        }

        println(getProgramsInGroup(programs))
        println(getTotalGroups(programs))
    }

    private fun getTotalGroups(programs: ArrayList<ArrayList<Int>>): Int {
        var groups = 0
        val parents = HashMap<Int, Set<Int>>()
        for (program in programs.indices) {
            var found = false
            for (parent in parents.keys) {
                val childs = parents[parent]
                if (childs?.contains(program)!!) {
                    found = true
                    break
                }
            }
            if (!found) {
                val visited = HashSet<Int>()
                getProgramsIn(program, programs, visited)
                parents.put(program, visited)
                groups++
            }
        }
        return groups
    }

    private fun getProgramsInGroup(programs: ArrayList<ArrayList<Int>>): Int {
        val visited = HashSet<Int>()
        return getProgramsIn(0, programs, visited)
    }

    private fun getProgramsIn(index: Int, programs: ArrayList<ArrayList<Int>>, visited: HashSet<Int>): Int {
        var count = 0
        for (child in programs[index]) {
            if (!visited.contains(child)) {
                visited.add(child)
                count += 1 + getProgramsIn(child, programs, visited)
            }
        }
        return count
    }

    private fun parsePrograms(data: String): ArrayList<Int> {
        val values = data.substring(data.indexOf("> ") + 2, data.length).split(", ")
        val array = ArrayList<Int>()
        for (value in values) {
            array.add(value.toInt())
        }
        return array
    }

}