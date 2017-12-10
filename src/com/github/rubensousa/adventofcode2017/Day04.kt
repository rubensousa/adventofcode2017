package com.github.rubensousa.adventofcode2017


import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.HashMap

object Day04 {

    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val bufferedReader = BufferedReader(FileReader("inputday04.txt"))
        var line: String? = bufferedReader.readLine()
        val passphrases = HashSet<String>()
        while (line != null) {
            passphrases.add(line)
            line = bufferedReader.readLine()
        }
        println(countValid(passphrases))
        println(countValidAnagrams(passphrases))
    }

    private fun countValid(passphrases: HashSet<String>): Int {
        var validPassphrases = 0
        val wordsSet = HashSet<String>()
        for (passphrase in passphrases) {
            val words = passphrase.split(" ".toRegex())
            var valid = true
            for (word in words) {
                if (wordsSet.contains(word)) {
                    valid = false
                    break
                } else {
                    wordsSet.add(word)
                }
            }
            if (valid) {
                validPassphrases++
            }
            wordsSet.clear()
        }
        return validPassphrases
    }

    private fun countValidAnagrams(passphrases: HashSet<String>): Int {
        var validPassphrases = 0
        val wordsSet = HashSet<String>()
        for (passphrase in passphrases) {
            val words = passphrase.split(" ".toRegex())
            var valid = true
            for (word in words) {
                if (wordsSet.contains(word)) {
                    valid = false
                    break
                } else {
                    wordsSet.add(word)
                }
            }
            if (valid && checkAnagrams(words)) {
                validPassphrases++
            }
            wordsSet.clear()
        }
        return validPassphrases
    }

    private fun checkAnagrams(words: List<String>): Boolean {
        for (i in 0 until words.size - 1) {
            val frequency = HashMap<Char, Int>()
            for (c in 0 until words[i].length) {
                val character = words[i][c]
                val freq = frequency[character]
                if (freq == null) {
                    frequency.put(character, 1)
                } else {
                    frequency.put(character, freq + 1)
                }
            }
            for (j in i + 1 until words.size) {
                if (words[j].length == words[i].length) {
                    val frequencyTable = HashMap<Char, Int>()
                    for (c in 0 until words[j].length) {
                        val character = words[j][c]
                        val freq = frequencyTable[character]
                        if (freq == null) {
                            frequencyTable.put(character, 1)
                        } else {
                            frequencyTable.put(character, freq + 1)
                        }
                    }
                    var equalFrequencies = 0
                    for (key in frequencyTable.keys) {
                        if (frequency.containsKey(key)) {
                            val f1 = frequencyTable[key]
                            val f2 = frequency[key]
                            if (f1 == f2) {
                                equalFrequencies++
                            }
                        } else {
                            break
                        }
                    }
                    if (equalFrequencies == frequencyTable.size) {
                        return false
                    }
                }
            }
        }
        return true
    }

}