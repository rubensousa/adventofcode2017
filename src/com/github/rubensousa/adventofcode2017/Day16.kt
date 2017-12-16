package com.github.rubensousa.adventofcode2017

import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*

object Day16 {

    val MOVE_SPIN = 0
    val MOVE_EXCHANGE = 1
    val MOVE_PARTNER = 2

    class Move {
        var moveType = 0
        var firstPosition = 0
        var secondPosition = 0
        var spinPosition = 0
        var firstChar = 'a'
        var secondChar = 'p'
    }

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        var programs = getPrograms(16)
        val moves = parseMoves()
        println(getOrder(programs, moves))
        programs = getPrograms(16)
        println(getOrderAfter(programs, moves, 1000000000))
    }

    private fun getOrder(programs: CharArray, moves: List<Move>): String {
        val builder = StringBuilder()
        val positions = HashMap<Char, Int>()

        for (i in programs.indices) {
            positions[programs[i]] = i
        }

        for (move in moves) {
            when (move.moveType) {
                MOVE_EXCHANGE -> performExchange(programs, positions, move)
                MOVE_PARTNER -> performPartner(programs, positions, move)
                MOVE_SPIN -> performSpin(programs, positions, move)
            }
        }

        for (i in programs.indices) {
            builder.append(programs[i])
        }

        return builder.toString()
    }

    private fun getOrderAfter(programs: CharArray, moves: List<Move>, times: Int): String {
        val builder = StringBuilder()
        val positions = HashMap<Char, Int>()
        val sequences = ArrayList<String>()
        var foundAgain = false
        var count = 0
        var first = ""

        for (i in programs.indices) {
            positions[programs[i]] = i
        }

        while (!foundAgain) {
            for (move in moves) {
                when (move.moveType) {
                    MOVE_EXCHANGE -> performExchange(programs, positions, move)
                    MOVE_PARTNER -> performPartner(programs, positions, move)
                    MOVE_SPIN -> performSpin(programs, positions, move)
                }
            }
            for (i in programs.indices) {
                builder.append(programs[i])
            }
            val sequence = builder.toString()
            when {
                sequences.isEmpty() -> first = sequence
                sequence.equals(first) -> foundAgain = true
            }
            if (!foundAgain) {
                sequences.add(sequence)
                builder.delete(0, builder.capacity())
                count++
            }
            if (count >= times) {
                foundAgain = true
            }
        }

        return if (count >= times) {
            sequences[count - 1]
        } else {
            sequences[(times - 1) % sequences.size]
        }
    }

    private fun performSpin(programs: CharArray, positions: HashMap<Char, Int>, move: Move) {
        val stack = Stack<Char>()
        for (i in 0 until move.spinPosition) {
            stack.add(programs[programs.lastIndex - i])
        }
        for (i in programs.lastIndex downTo move.spinPosition) {
            programs[i] = programs[i - move.spinPosition]
            positions[programs[i]] = i
        }
        for (i in 0 until move.spinPosition) {
            programs[i] = stack.pop()
            positions[programs[i]] = i
        }
    }

    private fun performPartner(programs: CharArray, positions: HashMap<Char, Int>, move: Move) {
        val position = positions[move.firstChar]
        val secondPosition = positions[move.secondChar]
        positions[move.firstChar] = secondPosition!!
        positions[move.secondChar] = position!!
        programs[secondPosition] = move.firstChar
        programs[position] = move.secondChar
    }

    private fun performExchange(programs: CharArray, positions: HashMap<Char, Int>, move: Move) {
        val temp = programs[move.firstPosition]
        positions[temp] = move.secondPosition
        positions[programs[move.secondPosition]] = move.firstPosition
        programs[move.firstPosition] = programs[move.secondPosition]
        programs[move.secondPosition] = temp
    }

    private fun getPrograms(size: Int): CharArray {
        val programs = CharArray(size)
        for (i in programs.indices) {
            programs[i] = 'a' + i
        }
        return programs
    }

    private fun parseMoves(): List<Move> {
        val moves = ArrayList<Move>()
        val bufferedReader = BufferedReader(FileReader("inputday16.txt"))
        val line: String? = bufferedReader.readLine()
        if (line != null) {
            val dataMoves = line.split(",")
            for (i in dataMoves.indices) {
                val rawMove = dataMoves[i]
                val move = Move()
                when (rawMove[0]) {
                    'p' -> {
                        move.moveType = MOVE_PARTNER
                        move.firstChar = rawMove[1]
                        move.secondChar = rawMove[3]
                    }
                    's' -> {
                        move.moveType = MOVE_SPIN
                        move.spinPosition = Integer.parseInt(rawMove.substring(1, rawMove.length))
                    }
                    'x' -> {
                        val slashIndex = rawMove.indexOf("/")
                        move.moveType = MOVE_EXCHANGE
                        move.firstPosition = Integer.parseInt(rawMove.substring(1, slashIndex))
                        move.secondPosition = Integer.parseInt(rawMove.substring(slashIndex + 1, rawMove.length))
                    }
                }
                moves.add(move)
            }
        }
        return moves
    }
}