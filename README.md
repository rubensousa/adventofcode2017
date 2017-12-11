# adventofcode2017

Solutions for Advent of Code 2017 edition: http://adventofcode.com/2017

## Day 01

- Part 1:

We just need to loop through each digit and check if the next one equals the current one. If this is true, we increment the sum. There's an edge case: the last position. In this case, we make the position point to the beginning:

```kotlin
var nextIndex = i + 1
if (nextIndex >= number.length) {
    nextIndex -= number.length
}
```
```
Time - O(n)
Space - O(1)
```

- Part 2:

Same thing, but instead of checking the next digit, we check the one n/2 steps ahead.

```kotlin
var nextIndex = i + number.length/2
```

## Day 02

- Part 1

Go through each line and find its minimum and maximum values. Then increment the sum with (maximum-minimum).

```
Time - O(nm) (n - lines / m - columns)
Space - O(1)
```

- Part 2

Go through each element in the line and check the ones after it. If they evenly divide, then increment the sum.

```kotlin
for (j in 0 until array[i].size - 1) {
    val num = array[i][j]
    for (k in j + 1 until array[i].size) {
        val num2 = array[i][k]
        val max = Math.max(num2, num)
        val min = Math.min(num2, num)
        if (max % min == 0) {
            sum += max / min
            break
        }
    }
}
```

```
Time - O(nm²) 
Space - O(1)
```
