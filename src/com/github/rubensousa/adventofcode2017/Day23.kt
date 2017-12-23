package com.github.rubensousa.adventofcode2017

import java.io.BufferedReader
import java.io.FileReader
import java.util.*


object Day23 {

    val INSTRUCTION_SET = "set"
    val INSTRUCTION_MUL = "mul"
    val INSTRUCTION_SUB = "sub"
    val INSTRUCTION_JNZ = "jnz"

    data class InstructionResult(val offset: Long)
    data class Instruction(val type: String, val firstRegister: String, val secondRegister: String)

    fun parseInstructions(): ArrayList<Instruction> {
        val instructions = ArrayList<Instruction>()
        val reader = BufferedReader(FileReader("inputday23.txt"))
        var line = reader.readLine()
        while (line != null) {
            val data = line.split(" ")
            val instruction = Instruction(data[0], data[1], if (data.lastIndex == 2) data[2] else "")
            instructions.add(instruction)
            line = reader.readLine()
        }
        return instructions
    }

    fun parseRegisters(instructions: List<Instruction>): HashMap<String, Long> {
        val registers = HashMap<String, Long>()
        for (instruction in instructions) {
            if (instruction.firstRegister[0] in 'a'..'z') {
                registers.put(instruction.firstRegister, 0)
            }
            if (instruction.secondRegister.isNotEmpty() && instruction.secondRegister[0] in 'a'..'z') {
                registers.put(instruction.secondRegister, 0)
            }
        }
        return registers
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val instructions = parseInstructions()
        println(getMultiplyInstructionsTimes(parseRegisters(instructions), instructions))
        val registers = parseRegisters(instructions)
        registers.put("a", 1)
        println(getValueInH())
    }

    private fun getValue(registers: HashMap<String, Long>, data: String): Long {
        return if (data[0] in 'a'..'z') {
            registers[data]!!
        } else {
            data.toLong()
        }
    }

    private fun getMultiplyInstructionsTimes(registers: HashMap<String, Long>,
                                             instructions: List<Instruction>): Long {
        var count = 0L
        var index = 0L
        while (index < instructions.size) {
            val instruction = instructions[index.toInt()]
            if (instruction.type == INSTRUCTION_MUL) {
                count++
            }
            val result = processInstruction(instruction, registers)
            index += result.offset
        }
        return count
    }

    private fun getValueInH(): Long {
        var b = 79L * 100 + 100000L
        val c = b + 17000
        var d: Long
        var f: Long
        var g: Long
        var h = 0L
        do {
            f = 1
            d = 2
            while (d < b) {
                if (b.rem(d) == 0L) {
                    f = 0L
                    break
                }
                d++
            }
            if (f == 0L) {
                h++
            }
            g = b - c
            b += 17
        } while (g != 0L)
        return h
    }

    private fun processInstruction(instruction: Instruction, registers: HashMap<String, Long>)
            : InstructionResult {
        var index = 1L
        when (instruction.type) {
            INSTRUCTION_SET -> registers.put(instruction.firstRegister, getValue(registers, instruction.secondRegister))
            INSTRUCTION_MUL -> registers.put(instruction.firstRegister, registers[instruction.firstRegister]!!
                    .times(getValue(registers, instruction.secondRegister)))
            INSTRUCTION_SUB -> registers.put(instruction.firstRegister, registers[instruction.firstRegister]!!
                    - getValue(registers, instruction.secondRegister))
            INSTRUCTION_JNZ -> {
                val value = getValue(registers, instruction.firstRegister)
                if (value != 0L) {
                    index += getValue(registers, instruction.secondRegister) - 1
                }
            }
        }
        return InstructionResult(index)
    }

    private fun originalAssembly() {
        var b = 0L
        var c = 0L
        var d = 0L
        var e = 0L
        var f = 0L
        var g = 0L
        var h = 0L
        b = 79
        b *= 100
        b += 100000
        c = b
        c += 17000
        do {
            f = 1
            d = 2
            e = 2
            do {
                do {
                    g = d
                    g *= e
                    g -= b
                    if (g == 0L) {
                        f = 0L
                    }
                    e++
                    g = e
                    g -= b
                    // d * e - b = 0 <> d*e = b
                } while (g != 0L)
                d++
                g = d
                g -= b
                // d - b = 0
            } while (g != 0L)
            if (f == 0L) {
                h++
            }
            g = b
            g -= c
            b -= 17
        } while (g != 0L)
    }

}