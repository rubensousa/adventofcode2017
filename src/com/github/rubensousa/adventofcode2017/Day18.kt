package com.github.rubensousa.adventofcode2017

import java.io.BufferedReader
import java.io.FileReader
import java.util.*
import kotlin.collections.HashMap


object Day18 {

    val INSTRUCTION_SET = "set"
    val INSTRUCTION_SND = "snd"
    val INSTRUCTION_MUL = "mul"
    val INSTRUCTION_ADD = "add"
    val INSTRUCTION_MOD = "mod"
    val INSTRUCTION_RCV = "rcv"
    val INSTRUCTION_JGZ = "jgz"

    data class InstructionResult(val offset: Long, val sent: Boolean, val waiting: Boolean)
    data class Instruction(val type: String, val firstRegister: String, val secondRegister: String)

    fun parseInstructions(): ArrayList<Instruction> {
        val instructions = ArrayList<Instruction>()
        val reader = BufferedReader(FileReader("inputday18.txt"))
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
        println(getRecoveryFrequency(parseRegisters(instructions), instructions))
        val registers0 = parseRegisters(instructions)
        val registers1 = parseRegisters(instructions)
        registers1.put("p", 1)
        println(getValuesSentByFirstProgram(registers0, registers1, instructions))
    }

    private fun getValue(registers: HashMap<String, Long>, data: String): Long {
        return if (data[0] in 'a'..'z') {
            registers[data]!!
        } else {
            data.toLong()
        }
    }

    private fun getRecoveryFrequency(registers: HashMap<String, Long>,
                                     instructions: List<Instruction>): Long {
        var sound = 0L
        var index = 0L
        while (index < instructions.size) {
            val instruction = instructions[index.toInt()]
            val result = processInstruction(instruction, registers, null, null)
            if (result.sent) {
                sound = registers[instruction.firstRegister]!!
            } else if (result.waiting) {
                return sound
            }
            index += result.offset
        }
        return sound
    }

    private fun getValuesSentByFirstProgram(registers0: HashMap<String, Long>,
                                            registers1: HashMap<String, Long>,
                                            instructions: List<Instruction>): Int {
        var index0 = 0L
        var index1 = 0L
        val queue0 = ArrayDeque<Long>()
        val queue1 = ArrayDeque<Long>()
        var timesSent = 0
        while (true) {
            val instruction0 = instructions[index0.toInt()]
            val instruction1 = instructions[index1.toInt()]
            val instruction0Result = processInstruction(instruction0, registers0, queue0, queue1)
            val instruction1Result = processInstruction(instruction1, registers1, queue1, queue0)
            if (instruction1Result.sent) {
                timesSent++
            }
            index0 += instruction0Result.offset
            index1 += instruction1Result.offset
            if (instruction0Result.waiting && instruction1Result.waiting) {
                break
            }
            if (index0 >= instructions.size || index1 >= instructions.size) {
                break
            }
        }
        return timesSent
    }

    private fun processInstruction(instruction: Instruction, registers: HashMap<String, Long>,
                                   receivingQueue: ArrayDeque<Long>?, sendingQueue: ArrayDeque<Long>?)
            : InstructionResult {
        var index = 1L
        when (instruction.type) {
            INSTRUCTION_SET -> registers.put(instruction.firstRegister, getValue(registers, instruction.secondRegister))
            INSTRUCTION_ADD -> registers.put(instruction.firstRegister, registers[instruction.firstRegister]!!
                    + getValue(registers, instruction.secondRegister))
            INSTRUCTION_MUL -> registers.put(instruction.firstRegister, registers[instruction.firstRegister]!!
                    .times(getValue(registers, instruction.secondRegister)))
            INSTRUCTION_MOD -> registers.put(instruction.firstRegister, registers[instruction.firstRegister]!!
                    .rem(getValue(registers, instruction.secondRegister)))
            INSTRUCTION_JGZ -> {
                val value = getValue(registers, instruction.firstRegister)
                if (value > 0) {
                    index += getValue(registers, instruction.secondRegister) - 1
                }
            }
            INSTRUCTION_SND -> {
                sendingQueue?.add(getValue(registers, instruction.firstRegister))
                return InstructionResult(1, true, false)
            }
            INSTRUCTION_RCV -> {
                if (receivingQueue != null) {
                    if (receivingQueue.isEmpty()) {
                        return InstructionResult(0, false, true)
                    }
                    val value = receivingQueue.remove()
                    registers.put(instruction.firstRegister, value)
                } else {
                    return InstructionResult(1, false, true)
                }
            }
        }
        return InstructionResult(index, false, false)
    }
}