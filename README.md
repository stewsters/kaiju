# KAIJU

## Kotlin Artificial Intelligence Java Utilities

How's that for a backronym?

This is some utilities I extracted from some simple kotlin roguelikes I was working on. The syntax works a bit better
than regular Java for algorithm heavy stuff like this.

In here is some things like pathfinding, serializing, and some matrix and vector stuff.

## How to use this in your project

Add Jitpack and the repo to your build.gradle or pom file.

Instruction can be found here:

[![](https://jitpack.io/v/stewsters/kaiju.svg)](https://jitpack.io/#stewsters/kaiju)

## What's Here

### Math Primitives

#### Immutable 2d and 3d integer vectors (Vec2 and Vec3)

```kotlin
 println(Vec2(1, 2) + Vec2(2, 3))
// (3, 5)

println(Vec3(1, 2, 0) - Vec3(2, 3, 1))
// (-1, -1, -1)

```

#### Matrices / multidimensional arrays for 2d and 3d

```kotlin
val mat = Matrix2d(10, 10) { x, y -> x * y }

assert(mat[3, 2] == 6)
assert(mat[Vec2[3, 3]] == 9)

mat[3, 2] = 0
assert(mat[3, 2] == 0)


```

#### Facing

```kotlin
val location = Vec2[0, 0] + Facing.EAST + Facing.NORTH

assert(location.x == 1)
assert(location.y == 1)
```

#### Rng

```kotlin
// For a source of randomness, I use the excellent pcg32
val ran = Pcg32()
ran.nextInt(10)

// get a number in a range
getIntInRange(0, 10)
getInt((0 until 10))
getInt((0..10)) // last number exclusive

getFloatInRange(0f, 10f)
getBoolean()

// Some dice
d(20) // Roll a die
4.d(6) // roll 4 six sided dice and add them


```

### Pathfinding

#### AStar

Pathfinding from one spot to another in 2d and 3d.

```kotlin

val size = Vec2[20, 20]

// make a field that has a wall with a hole in it
val field = Matrix2d(size) { x, y -> x == 6 && y != 0 }

// find a path
val path = findPath2d(
    field.getSize(),
    { if (field[it]) 100000.0 else 1.0 },
    ::getEuclideanDistance,
    { it.vonNeumanNeighborhood() },
    Vec2[0, 0],
    Vec2[19, 19]
)

```

#### Djikstra Maps

Djikstra maps build a distance field from a set of points. It is useful to generate flow for multiple unit pathfinding
to many targets in one step.

```kotlin

val size = Vec2[10, 10]

// Find distance to either [5, 5] or [2, 2], where x > 6 is blocked
val map = dijkstraMap2d(size, listOf(Vec2[5, 5], Vec2[2, 2]), { pos -> pos.x > 6 })

```

### Helper Methods

JSON serialization

## Future Plans

* Behavior Trees
* Monte Carlo Tree Search (MCTS)


