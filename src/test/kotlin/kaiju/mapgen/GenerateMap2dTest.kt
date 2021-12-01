package kaiju.mapgen

import kaiju.mapgen.noise.NoiseFunction2d
import kaiju.mapgen.two.brush.drawCell
import kaiju.mapgen.two.fill
import kaiju.mapgen.two.fillWithBorder
import kaiju.mapgen.two.floodFill
import kaiju.mapgen.two.predicate.*
import kaiju.math.*
import kaiju.math.geom.Rectangle
import kaiju.pathfinder.findPath2d
import org.junit.Test
import kotlin.math.cos
import kotlin.math.sin


class GenerateMap2dTest {
    @Test
    fun testGenerationOfBoxViaPredicates() {
        val em1 = matrix2dOf(20, 20) { x, y -> unknown }
        fillWithBorder(em1, floor, wall)

        val em2 = matrix2dOf(20, 20) { x, y -> unknown }
        fill(em2, cellNearEdge(), drawCell(wall))
        fill(em2, not(cellNearEdge()), drawCell(floor))

        testEquality(em1, em2)
        println("BOX 1 - borderfill")
        printMap(em1)
        println("Box 2 - Fill with CellNearEdge and notPredicates")
        printMap(em2)
    }

    @Test
    fun testGenerationOfTreesViaPredicates() {
        val em = matrix2dOf(20, 20) { x, y -> unknown }
        fill(em, cellNearEdge(), drawCell(wall))
        fill(em, not(cellNearEdge()), drawCell(floor))
        println("Gen trees 1 - not planted yet")
        printMap(em)
        val vegetation = NoiseFunction2d(10.0, 30.0, 16.0, 13.0)
        val tree = ExampleCellType('T', true)
        fill(
            em,
            and(
                noiseGreaterThan(vegetation, 0.5),
                cellEquals(floor)
            ),
            drawCell(tree)
        )

        val snow = NoiseFunction2d(-10.0, -500.0, 20.0, 22.0)
        val pineTree = ExampleCellType('p', true)
        fill(
            em, and(
                noiseGreaterThan(snow, 0.6),
                cellEquals(tree)
            ), drawCell(pineTree)
        )
        println("Gen trees 2 - you should start seeing T's")
        printMap(em)
    }

    @Test
    fun testOr() {
        val em = matrix2dOf(10, 10) { x, y -> grass }
        em[5, 5] = floor

        fill(
            em,
            or(
                cellNearEdge(),
                nearCell(floor)
            ),
            drawCell(wall)
        )
        printMap(em)
    }

    @Test
    fun testFloodFill() {
        val em = matrix2dOf(10, 10) { x, y -> unknown }
        val center = Vec2(5, 5)
        val wallPoints: List<Vec2> = center.mooreNeighborhood()

        //Draw a wall
        fill(em,
            { map: Matrix2d<ExampleCellType>, x: Int, y: Int -> wallPoints.contains(Vec2(x, y)) },
            { m, x, y -> m[x, y] = wall }
        )
        assert(em[5, 5] === unknown)
        for (wallPoint in wallPoints) {
            assert(em[wallPoint.x, wallPoint.y] === wall)
        }


        // Floodfill inside that wall and make sure it doesnt escape
        floodFill(em, center, cellEquals(unknown), drawCell(floor))
        assert(em[5, 5] === floor)
        for (wallPoint in wallPoints) {
            assert(em[wallPoint.x, wallPoint.y] === wall)
        }
        assert(em[0, 0] === unknown)
        printMap(em)
    }

    @Test
    fun makeAVillageByALake() {
        val map = matrix2dOf(100, 100) { x, y -> unknown }
        fillWithBorder(map, grass, forest)
        val xMid: Int = map.xSize / 2
        val yMid: Int = map.ySize / 2
        val buildingRadius = xMid / 2.toDouble()
        val lakeRadius = buildingRadius / 2
        val forestRadius = buildingRadius + 1
        val rooms: List<Rectangle> = (0..10).map { i: Int ->
            val angle: Float = getFloatInRange(0f, Math.PI.toFloat() * 2)
            val x: Int = xMid + (buildingRadius * cos(angle.toDouble())).toInt() + getIntInRange(-3, 3)
            val y: Int = yMid + (buildingRadius * sin(angle.toDouble())).toInt() + getIntInRange(-3, 3)
            val xw: Int = getIntInRange(2, 3)
            val yw: Int = getIntInRange(2, 3)
            Rectangle(Vec2(x - xw, y - yw), Vec2(x + xw, y + yw))
        }
        rooms.forEach { room: Rectangle ->
            fill(
                map,
                containedIn(room),
                drawCell(floor)
            )
        }

        //Put walls around buildings
        fill(
            map,
            and(
                nearCell(floor),
                cellEquals(grass)
            ),
            drawCell(wall)
        )

        // Fill in pond
        val lake = NoiseFunction2d(30.0, 20.0, 24.0, 24.0)
        fill(map,
            and(
                cellEquals(grass),
                { e: Matrix2d<ExampleCellType>, x: Int, y: Int ->
                    0.1 * lake.gen(
                        x.toDouble(),
                        y.toDouble()
                    ) + 0.9 * ((lakeRadius - getEuclideanDistance(
                        xMid.toDouble(),
                        yMid.toDouble(),
                        x.toDouble(),
                        y.toDouble()
                    )) / xMid.toFloat()) > 0
                }
            ),
            drawCell(water)
        )
        val trees = NoiseFunction2d(10.0, 30.0, 4.0, 4.0)
        fill(map,
            and(
                cellEquals(grass),
                { e, x, y ->
                    trees.gen(
                        x.toDouble(),
                        y.toDouble()
                    ) + (forestRadius - getEuclideanDistance(
                        xMid.toDouble(),
                        yMid.toDouble(),
                        x.toDouble(),
                        y.toDouble()
                    )) / xMid.toFloat() < 0
                }
            ),
            drawCell(forest)
        )

        // Dig paths
        val centers: List<Vec2> = rooms.map { room: Rectangle -> room.center() }
        var centerLast = Vec2(xMid, 0)
        for (center in centers) {

            val path: List<Vec2>? = findPath2d(
                map.getSize(),
                { v ->
                    when (map[v.x, v.y]) {
                        wall -> 10.0
                        floor -> 2.0
                        grass -> 1.0
                        forest -> 2.0
                        water -> 30.0
                        else -> 1.0
                    }
                },
                ::getEuclideanDistance,
                { it.vonNeumanNeighborhood() },
                centerLast, center
            )
            centerLast = center


            path?.forEach { it: Vec2 ->
                val cellType: ExampleCellType = map[it]
                map[it] = if (cellType === grass || cellType === forest) {
                    grass
                } else if (cellType === wall || cellType === door) {
                    door
                } else {
                    floor
                }
            }
            //            }

//            fill(em, (e, x, y) -> false,
//                    (e, x, y) -> {
//                    }
//            );
        }
        printMap(map)
    }

    private fun <T> testEquality(map1: Matrix2d<T>, map2: Matrix2d<T>) {
        assert(map1.getSize() == map2.getSize())

        for (y in 0 until map1.xSize) {
            for (x in 0 until map1.ySize) {
                assert(map1[x, y] === map2[x, y])
            }
        }
    }

    private fun printMap(exampleGeneratedMap2D: Matrix2d<ExampleCellType>) {
        for (y in 0 until exampleGeneratedMap2D.xSize) {
            for (x in 0 until exampleGeneratedMap2D.ySize) {
                print(exampleGeneratedMap2D[x, y].glyph)
            }
            println()
        }
    }

    companion object {
        val unknown: ExampleCellType = ExampleCellType('?', true)
        val wall: ExampleCellType = ExampleCellType('X', true)
        val floor: ExampleCellType = ExampleCellType('.', false)
        val grass: ExampleCellType = ExampleCellType(',', false)
        val forest: ExampleCellType = ExampleCellType('T', true)
        val water: ExampleCellType = ExampleCellType('w', true)
        val door: ExampleCellType = ExampleCellType('D', false)
    }
}

data class ExampleCellType(val glyph: Char, val blocks: Boolean)