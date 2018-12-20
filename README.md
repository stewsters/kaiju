# KAIJU

## Kotlin Artificial Intelligence Java Utilities

Hows that for a backronym?

This is some utilities I extracted from some simple kotlin roguelikes I was working on.  The syntax works a bit better
 than regular Java for algorithm heavy stuff like this.  
 
In here is some things like pathfinding, serializing, and some matrix and vector stuff.

## How to use this in your project

Add Jitpack and the repo to your build.gradle or pom file.

Instruction can be found here:

[![](https://jitpack.io/v/stewsters/kaiju.svg)](https://jitpack.io/#stewsters/kaiju)

## Whats Here


### Math Primitives

#### Cached immutable 2d and 3d vectors (Vec2 and Vec3)

```kotlin
 println(Vec2[1,2] + Vec2[2,3])
 // (3, 5)
 
 println(Vec2[1,2,0] - Vec2[2,3,1])
 // (-1, -1, -1)

```


#### Matrices / multidimensional arrays for 2d and 3d

```kotlin
val mat = Matrix2d(10, 10) { x, y -> x * y }

assert(mat[3,2] == 6)  
assert(mat[Vec2[3,3]] == 9)

mat[3,2] = 0
assert(mat[3,2] == 0) 


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

// Random selection from a list or array
rand(listOf(1, 2, 3, 4))
rand(arrayOf(5, 6, 7, 8))


```


### Pathfinding

#### AStar


#### Djikstra Maps



### Helper Methods

JSON serialization


## Future Plans

* Behavior Trees
* Monte Carlo Tree Search (MCTS)


