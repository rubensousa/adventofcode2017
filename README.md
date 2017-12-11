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

Time - O(n)

Space - O(1)

- Part 2:

Same thing, but instead of checking the next digit, we check the one n/2 steps ahead.

```kotlin
var nextIndex = i + number.length/2
```
