package com.github.rubensousa.adventofcode2017


import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object Day08 {

    val GREATER_THAN = ">"
    val GREATER_OR_EQUAL_THAN = ">="
    val LESSER_THAN = "<"
    val LESSER_OR_EQUAL_THAN = "<="
    val EQUAL = "=="
    val NOT_EQUAL = "!="

    class Instruction {
        var register = ""
        var incrementValue = 0
        var increment = true
        var conditionalRegister = ""
        var conditionalValue = 0
        var conditionalOp = GREATER_THAN
    }

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val bufferedReader = BufferedReader(FileReader("inputday08.txt"))
        var line: String? = bufferedReader.readLine()
        val instructions = ArrayList<Instruction>()
        val registers = HashMap<String, Int>()

        while (line != null) {
            val instruction = parseInstruction(line)
            registers.put(instruction.register, 0)
            instructions.add(instruction)
            line = bufferedReader.readLine()
        }

        println(getGreatestRegister(instructions, registers))

        for (key in registers.keys) {
            registers.put(key, 0)
        }

        println(getGreatestRegisterDuring(instructions, registers))
    }

    private fun getGreatestRegister(instructions: ArrayList<Instruction>,
                                    registers: HashMap<String, Int>): Int {

        for (instruction in instructions) {
            var execute = false
            when (instruction.conditionalOp) {
                GREATER_OR_EQUAL_THAN -> {
                    execute = registers[instruction.conditionalRegister]!! >= instruction.conditionalValue
                }
                GREATER_THAN -> {
                    execute = registers[instruction.conditionalRegister]!! > instruction.conditionalValue
                }
                EQUAL -> {
                    execute = registers[instruction.conditionalRegister]!! == instruction.conditionalValue
                }
                NOT_EQUAL -> {
                    execute = registers[instruction.conditionalRegister]!! != instruction.conditionalValue
                }
                LESSER_THAN -> {
                    execute = registers[instruction.conditionalRegister]!! < instruction.conditionalValue
                }
                LESSER_OR_EQUAL_THAN -> {
                    execute = registers[instruction.conditionalRegister]!! <= instruction.conditionalValue
                }
            }
            if (execute) {
                if (instruction.increment) {
                    registers.put(instruction.register, registers[instruction.register]!! + instruction.incrementValue)
                } else {
                    registers.put(instruction.register, registers[instruction.register]!! - instruction.incrementValue)
                }
            }
        }

        var max = 0

        for (value in registers.values) {
            max = Math.max(max, value)
        }

        return max
    }

    private fun getGreatestRegisterDuring(instructions: ArrayList<Instruction>,
                                          registers: HashMap<String, Int>): Int {
        var max = 0

        for (instruction in instructions) {
            var execute = false
            when (instruction.conditionalOp) {
                GREATER_OR_EQUAL_THAN -> {
                    execute = registers[instruction.conditionalRegister]!! >= instruction.conditionalValue
                }
                GREATER_THAN -> {
                    execute = registers[instruction.conditionalRegister]!! > instruction.conditionalValue
                }
                EQUAL -> {
                    execute = registers[instruction.conditionalRegister]!! == instruction.conditionalValue
                }
                NOT_EQUAL -> {
                    execute = registers[instruction.conditionalRegister]!! != instruction.conditionalValue
                }
                LESSER_THAN -> {
                    execute = registers[instruction.conditionalRegister]!! < instruction.conditionalValue
                }
                LESSER_OR_EQUAL_THAN -> {
                    execute = registers[instruction.conditionalRegister]!! <= instruction.conditionalValue
                }
            }
            if (execute) {
                if (instruction.increment) {
                    registers.put(instruction.register, registers[instruction.register]!! + instruction.incrementValue)
                } else {
                    registers.put(instruction.register, registers[instruction.register]!! - instruction.incrementValue)
                }
            }
            max = Math.max(max, registers[instruction.register]!!)
        }

        return max
    }

    private fun parseInstruction(line: String): Instruction {
        val fields = line.split(" ")
        val instruction = Instruction()
        instruction.register = fields[0]
        instruction.increment = fields[1].equals("inc")
        instruction.incrementValue = Integer.parseInt(fields[2])
        instruction.conditionalRegister = fields[4]
        instruction.conditionalOp = fields[5]
        instruction.conditionalValue = Integer.parseInt(fields[6])
        return instruction
    }

}