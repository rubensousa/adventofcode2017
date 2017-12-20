package com.github.rubensousa.adventofcode2017

import java.io.BufferedReader
import java.io.FileReader

object Day20 {

    data class EquationResult(var result1: Double, var result2: Double)

    data class Particle(var id: Long, var x: Long, var y: Long, var z: Long, var velocityX: Long,
                        var velocityY: Long, var velocityZ: Long,
                        val accelX: Long, val accelY: Long, val accelZ: Long) {

        object COORDINATE {
            val x = 0
            val y = 1
            val z = 2
        }

        var originalX: Long = x
        var originalY: Long = y
        var originalZ: Long = z

        fun distanceToOrigin(): Long {
            return Math.abs(x) + Math.abs(y) + Math.abs(z)
        }

        fun reset() {
            x = originalX
            y = originalY
            z = originalZ
        }
    }

    private fun parseParticles(): ArrayList<Particle> {
        val reader = BufferedReader(FileReader("inputday20.txt"))
        val lines = ArrayList<String>()
        val particles = ArrayList<Particle>()
        var line = reader.readLine()
        val pattern = Regex("(?:<(.*?)>)")
        val numbers = ArrayList<Long>()
        var id = 0L
        while (line != null) {
            val matchResults = pattern.findAll(line).toList()
            for (group in matchResults.indices) {
                val data = matchResults[group].groups[1]?.value?.split(",")
                if (data != null) {
                    for (string in data) {
                        numbers.add(string.toLong())
                    }
                }
            }
            lines.add(line)
            line = reader.readLine()
            particles.add(Particle(id, numbers[0], numbers[1], numbers[2], numbers[3], numbers[4], numbers[5],
                    numbers[6], numbers[7], numbers[8]))
            numbers.clear()
            id++
        }
        return particles
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val particles = parseParticles()
        println(getNearestToZero(particles))
        for (particle in particles) {
            particle.reset()
        }
        println(getCollisions(particles))
    }

    private fun getNearestToZero(particles: ArrayList<Particle>): Int {
        var minParticle = particles[0]
        var minIndex = 0
        for (i in particles.indices) {
            moveParticle(particles[i], 100000)
            if (particles[i].distanceToOrigin() < minParticle.distanceToOrigin()) {
                minParticle = particles[i]
                minIndex = i
            }
        }
        return minIndex
    }

    /**
     * Arithmetic series
     * s -> steps
     * V(s) = V0 + s*A
     * X(s) = X0 + sum of previous V(s)
     * X(s) = X0 + s/2 * (V0 + V(s+1))
     * X(s) = X0 + s/2 * (2V0 + (s+1)*A)
     */
    private fun moveParticle(particle: Particle, steps: Long) {
        particle.x = particle.originalX + steps / 2 * (particle.velocityX * 2 + (steps + 1) * particle.accelX)
        particle.y = particle.originalY + steps / 2 * (particle.velocityY * 2 + (steps + 1) * particle.accelY)
        particle.z = particle.originalZ + steps / 2 * (particle.velocityZ * 2 + (steps + 1) * particle.accelZ)
    }

    private fun getCollisions(particles: ArrayList<Particle>): Int {
        val particleSet = LinkedHashSet<Particle>(particles)
        for (i in 0 until particles.size - 1) {
            for (j in i + 1 until particles.size) {
                if (willParticlesCollide(particles[i], particles[j])) {
                    particleSet.remove(particles[i])
                    particleSet.remove(particles[j])
                }
            }
        }
        return particleSet.size
    }

    private fun willParticlesCollide(particle1: Particle, particle2: Particle): Boolean {
        val collisionX = getEquationResult(particle1, particle2, Particle.COORDINATE.x)
        val collisionY = getEquationResult(particle1, particle2, Particle.COORDINATE.y)
        val collisionZ = getEquationResult(particle1, particle2, Particle.COORDINATE.z)
        return match(collisionX, collisionY, collisionZ)
    }

    private fun getEquationResult(particle1: Particle, particle2: Particle, coordinate: Int): EquationResult {
        var a = 0L
        var b = 0L
        var c = 0L
        when (coordinate) {
            Particle.COORDINATE.x -> {
                a = particle1.accelX - particle2.accelX
                b = particle1.accelX + 2 * particle1.velocityX - particle2.accelX - 2 * particle2.velocityX
                c = 2 * (particle1.originalX - particle2.originalX)
            }
            Particle.COORDINATE.y -> {
                a = particle1.accelY - particle2.accelY
                b = particle1.accelY + 2 * particle1.velocityY - particle2.accelY - 2 * particle2.velocityY
                c = 2 * (particle1.originalY - particle2.originalY)
            }
            Particle.COORDINATE.z -> {
                a = particle1.accelZ - particle2.accelZ
                b = particle1.accelZ + 2 * particle1.velocityZ - particle2.accelZ - 2 * particle2.velocityZ
                c = 2 * (particle1.originalZ - particle2.originalZ)
            }
        }
        if (a == 0L) {
            return EquationResult(-c / b.toDouble(), Double.NaN)
        } else {
            var result1 = (-b + Math.sqrt(b * b - 4.0 * a * c)) / (2.0 * a)
            var result2 = (-b - Math.sqrt(b * b - 4.0 * a * c)) / (2.0 * a)
            if (result1.isInfinite()) {
                result1 = Double.NaN
            }
            if (result2.isInfinite()) {
                result2 = Double.NaN
            }
            return if (result1 > result2) {
                EquationResult(result1, result2)
            } else {
                EquationResult(result2, result1)
            }
        }
    }

    private fun match(collisionX: EquationResult, collisionY: EquationResult, collisionZ: EquationResult)
            : Boolean {
        val matches = HashMap<Double, Int>()

        if (!collisionX.result1.isNaN()) {
            matches.put(collisionX.result1, 1)
        }
        if (!collisionX.result2.isNaN()) {
            matches.put(collisionX.result2, 1)
        }

        if (!collisionY.result1.isNaN() && matches[collisionY.result1] != null) {
            matches.put(collisionY.result1, 2)
        }

        if (!collisionY.result2.isNaN() && matches[collisionY.result2] != null) {
            matches.put(collisionY.result2, 2)
        }

        if (!collisionZ.result1.isNaN() && matches[collisionZ.result1] != null) {
            matches.put(collisionZ.result1, matches[collisionZ.result1]!! + 1)
        }

        if (!collisionZ.result2.isNaN() && matches[collisionZ.result2] != null) {
            matches.put(collisionZ.result2, matches[collisionZ.result2]!! + 1)
        }

        return matches.keys.any { matches[it] == 3 }
    }


}